package com.esoft.gascollect.service;

import com.esoft.gascollect.dto.OrderDTO;
import com.esoft.gascollect.dto.OrderGasDTO;
import com.esoft.gascollect.dto.OutletOrderDTO;
import com.esoft.gascollect.entity.*;
import com.esoft.gascollect.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderGasRepository orderGasRepository;
    private final GasRepository gasRepository;
    private final UserRepository userRepository;
    private final OutletRepository outletRepository;
    private final DelieverySheduleRepository deliveryScheduleRepository;
    private final DeliveryGasScheduleRepository deliveryGasScheduleRepository;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            OrderGasRepository orderGasRepository,
            GasRepository gasRepository,
            UserRepository userRepository,
            OutletRepository outletRepository,
            DelieverySheduleRepository deliveryScheduleRepository,
            DeliveryGasScheduleRepository deliveryGasScheduleRepository) {
        this.orderRepository = orderRepository;
        this.orderGasRepository = orderGasRepository;
        this.gasRepository = gasRepository;
        this.userRepository = userRepository;
        this.outletRepository = outletRepository;
        this.deliveryScheduleRepository = deliveryScheduleRepository;
        this.deliveryGasScheduleRepository = deliveryGasScheduleRepository;
    }

    public Integer getLastOrderId() {
        return orderRepository.findLastOrderId().orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        String randomToken = UUID.randomUUID().toString();
        orderDTO.setTokenNumber(randomToken);
        BeanUtils.copyProperties(orderDTO, order);

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDTO.getUserId()));
        order.setUser(user);


        Outlet outlet = outletRepository.findById(orderDTO.getOutletId())
                .orElseThrow(() -> new RuntimeException("Outlet with id not found: " + orderDTO.getOutletId()));
        System.out.println("outlet--------------------------------------: " + outlet);
        order.setOutlet(outlet);
        Order savedOrder = orderRepository.save(order);

        // Process OrderGas items and adjust outlet stock
        for (OrderGasDTO orderGasDTO : orderDTO.getOrderGasList()) {
            OrderGas orderGas = new OrderGas();
            orderGas.setOrder(savedOrder);

            // Fetch the Gas entity
            Gas gas = gasRepository.findById(orderGasDTO.getGasId())
                    .orElseThrow(() -> new RuntimeException("Gas not found with id: " + orderGasDTO.getGasId()));

            orderGas.setGas(gas);
            orderGas.setQuantity(orderGasDTO.getQuantity());
            orderGasRepository.save(orderGas);

            // Adjust stock for the gas in the outlet
            boolean stockUpdated = false;
            for (OutletStock outletStock : outlet.getStock()) {
                if (outletStock.getGasId() == gas.getId()) {
                    // Check if sufficient stock is available
                    if (outletStock.getQuantity() < orderGasDTO.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for Gas ID: " + gas.getId());
                    }
                    // Deduct the stock
                    outletStock.setQuantity(outletStock.getQuantity() - orderGasDTO.getQuantity());
                    stockUpdated = true;
                    break;
                }
            }

            // If the gas is not found in outlet stock, throw an exception
            if (!stockUpdated) {
                throw new RuntimeException("Gas ID " + gas.getId() + " is not available in outlet stock.");
            }
        }

        // Save the updated outlet stock
        outletRepository.save(outlet);

        // Convert saved Order back to DTO
        return EntityToDTO(savedOrder);
    }

    public OrderDTO sellRequestedOrder(int id) {
        // Fetch the order by ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Check if the order contains any gas items
        if (!order.getOrderGasList().isEmpty()) {
            List<OrderGas> orderGases = order.getOrderGasList();
            Outlet outlet = order.getOutlet(); // Assume the order is associated with an outlet
            // Iterate over the order gases to reduce stock
            for (OrderGas orderGas : orderGases) {
                Gas gas = orderGas.getGas();
                int requiredQuantity = orderGas.getQuantity();

                boolean stockUpdated = false;
                for (OutletStock outletStock : outlet.getStock()) {
                    if (outletStock.getGasId() == gas.getId()) {
                        if (outletStock.getQuantity() < requiredQuantity) {
                            throw new RuntimeException("Insufficient stock for Gas ID: " + gas.getId());
                        }
                        outletStock.setQuantity(outletStock.getQuantity() - requiredQuantity);
                        stockUpdated = true;
                        break;
                    }
                }

                if (!stockUpdated) {
                    throw new RuntimeException("Gas ID " + gas.getId() + " is not available in outlet stock.");
                }
            }

            // Save the updated outlet stock
            outletRepository.save(outlet);

            // Update the order status to "Completed" or a relevant status
            order.setStatus("Delivered");
            orderRepository.save(order);
        } else {
            throw new RuntimeException("No gas items found in the order to process.");
        }

        // Convert the updated order to DTO and return
        return  EntityToDTO(order);
    }


    public OrderDTO createOrderWithDeliveryRequest(OrderDTO orderDTO) {
        String randomToken = UUID.randomUUID().toString();
        orderDTO.setTokenNumber(randomToken);
        Order order=DTOtoEntity(orderDTO);

        // Fetch the User entity using userId
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDTO.getUserId()));
        order.setUser(user); // Set the User entity to the Order
        order.setStatus("Requested"); // Set status as "Requested"


        // Fetch the Outlet entity using outletId
        Outlet outlet = outletRepository.findById(orderDTO.getOutletId())
                .orElseThrow(() -> new RuntimeException("Outlet with id not found: " + orderDTO.getOutletId()));
        order.setOutlet(outlet);
        Order savedOrder = orderRepository.save(order);

        // Save OrderGas items associated with the order
        List<OrderGasDTO> orderGasDTOs = orderDTO.getOrderGasList();
        for (OrderGasDTO orderGasDTO : orderGasDTOs) {
            OrderGas orderGas = new OrderGas();
            orderGas.setOrder(savedOrder);

            Gas gas = gasRepository.findById(orderGasDTO.getGasId())
                    .orElseThrow(() -> new RuntimeException("Gas not found with id: " + orderGasDTO.getGasId()));
            orderGas.setGas(gas);
            orderGas.setQuantity(orderGasDTO.getQuantity());
            orderGasRepository.save(orderGas);
        }

        // Check the last delivery schedule for the outlet
        DeliverySchedule lastDeliverySchedule = findLatestDeliveredSchedule(outlet);

        String todayDate = LocalDate.now().toString(); // Get today's date as a string
        boolean newDeliveryScheduleRequired = false;
        DeliverySchedule deliverySchedule = null;

        if (lastDeliverySchedule == null || lastDeliverySchedule.getNextDeliveryDate() == null ||
                LocalDate.parse(lastDeliverySchedule.getNextDeliveryDate()).isBefore(LocalDate.now())) {
            // If no valid last schedule exists or nextDeliveryDate is before today, create a new schedule
            newDeliveryScheduleRequired = true;
        } else {
            // Check if a schedule for the nextDeliveryDate exists
            Optional<DeliverySchedule> existingSchedule = findDeliveryScheduleByDateAndOutlet(lastDeliverySchedule.getNextDeliveryDate(), outlet);
            if (!existingSchedule.isPresent()) {
                newDeliveryScheduleRequired = true;
            } else {
                deliverySchedule = existingSchedule.get();
                order.setDeliverySchedule(deliverySchedule);
            }
        }
        //// --------------- add data first
        // Create a new delivery schedule if necessary
        if (newDeliveryScheduleRequired) {
            deliverySchedule = new DeliverySchedule();
            deliverySchedule.setOutlet(outlet);
            deliverySchedule.setDeliveryDate(lastDeliverySchedule != null ? lastDeliverySchedule.getNextDeliveryDate() : todayDate);
            deliverySchedule.setStatus("Pending");
            deliverySchedule.setDelivered(false);
            deliverySchedule.setNextDeliveryDate(LocalDate.now().plusDays(14).toString()); // Can be calculated later based on the schedule
            deliverySchedule = deliveryScheduleRepository.save(deliverySchedule);
            order.setDeliverySchedule(deliverySchedule);
        }

        // Update or create DeliveryScheduleGas entries
        for (OrderGasDTO orderGasDTO : orderGasDTOs) {
            Gas gas = gasRepository.findById(orderGasDTO.getGasId())
                    .orElseThrow(() -> new RuntimeException("Gas not found with id: " + orderGasDTO.getGasId()));

            // Check if the DeliveryScheduleGas exists for this gas
            DeliveryScheduleGas deliveryScheduleGas =
                    findDeliveryScheduleGas(deliverySchedule, gas)
                            .orElse(new DeliveryScheduleGas());

            deliveryScheduleGas.setDeliverySchedule(deliverySchedule);
            deliveryScheduleGas.setGas(gas);
            deliveryScheduleGas.setQuantity(
                    deliveryScheduleGas.getQuantity() + orderGasDTO.getQuantity());
            deliveryGasScheduleRepository.save(deliveryScheduleGas);
        }
        Order updatedOrder=orderRepository.save(order);
        // Convert saved Order back to DTO
        return  EntityToDTO(updatedOrder);
    }


    // Fetch the latest delivered schedule for an outlet
    public DeliverySchedule findLatestDeliveredSchedule(Outlet outlet) {
        List<DeliverySchedule> schedules = deliveryScheduleRepository.findAll();
        return schedules.stream()
                .filter(schedule -> schedule.getOutlet().getId() == outlet.getId() && schedule.isDelivered())
                .max((a, b) -> a.getDeliveryDate().compareTo(b.getDeliveryDate()))
                .orElse(null);
    }

    // Find a delivery schedule by date and outlet
    public Optional<DeliverySchedule> findDeliveryScheduleByDateAndOutlet(String deliveryDate, Outlet outlet) {
        List<DeliverySchedule> schedules = deliveryScheduleRepository.findAll();
        return schedules.stream()
                .filter(schedule -> schedule.getDeliveryDate().equals(deliveryDate) &&
                        schedule.getOutlet().getId() == outlet.getId())
                .findFirst();
    }

    // Find the corresponding DeliveryScheduleGas for the given delivery schedule and gas
    public Optional<DeliveryScheduleGas> findDeliveryScheduleGas(DeliverySchedule deliverySchedule, Gas gas) {
        List<DeliveryScheduleGas> scheduleGases = deliveryGasScheduleRepository.findAll();
        return scheduleGases.stream()
                .filter(scheduleGas -> scheduleGas.getDeliverySchedule().getId() == deliverySchedule.getId() &&
                        scheduleGas.getGas().getId() == gas.getId())
                .findFirst();
    }



    public OrderDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return  EntityToDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::EntityToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> getAllRequestedOrders() {
        return orderRepository.findAllByStatus("Requested").stream().map(this ::EntityToDTO).collect(Collectors.toList());
    }

    public List<OutletOrderDTO> getOrdersByStatusGroupedByOutlet() {
        List<Object[]> results = orderRepository.findOrdersByStatusGroupedByOutlet("Requested");

        // Create a list to hold the grouped data
        List<OutletOrderDTO> groupedOrders = new ArrayList<>();

        // Map each outlet and its orders
        Map<Outlet, List<OrderDTO>> outletOrdersMap = new HashMap<>();
        for (Object[] result : results) {
            Outlet outlet = (Outlet) result[0];
            Order order = (Order) result[1];

            OrderDTO orderDTO = EntityToDTO(order);

            outletOrdersMap.computeIfAbsent(outlet, k -> new ArrayList<>()).add(orderDTO);
        }

        // Convert the map to a list of OutletOrderDTO
        for (Map.Entry<Outlet, List<OrderDTO>> entry : outletOrdersMap.entrySet()) {
            Outlet outlet = entry.getKey();
            List<OrderDTO> orders = entry.getValue();

            // Create an OutletOrderDTO and add it to the list
            groupedOrders.add(new OutletOrderDTO(outlet.getId(), outlet.getName(), outlet.getLocation(), orders));
        }

        return groupedOrders;
    }

    public OrderDTO updateOrderStatus(int id, OrderDTO orderDTO) {
        // Find the existing order by ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }
        if (orderDTO.getTokenNumber() != null) {
            order.setTokenNumber(orderDTO.getTokenNumber());
        }


        // Save the updated order
        Order updatedOrder = orderRepository.save(order);

        // Convert the updated order to a DTO and return it
        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setId(updatedOrder.getId());
        updatedOrderDTO.setStatus(updatedOrder.getStatus());
        updatedOrderDTO.setTokenNumber(updatedOrder.getTokenNumber());
        updatedOrderDTO.setUserId(updatedOrder.getUser().getId());
        updatedOrderDTO.setDeliveryScheduleId(updatedOrder.getDeliverySchedule() != null ? updatedOrder.getDeliverySchedule().getId() : null);
        updatedOrderDTO.setOutletId(updatedOrder.getOutlet().getId());
        updatedOrderDTO.setOrderGasList(updatedOrder.getOrderGasList().stream()
                .map(orderGas -> new OrderGasDTO(orderGas.getId(), orderGas.getGas().getId(), orderGas.getQuantity()))
                .collect(Collectors.toList()));

        return updatedOrderDTO;
    }

    public OrderDTO updateOrder(int id, OrderDTO orderDTO) {
        // Find the existing order by ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Update only the fields that are provided in the DTO
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }
        if (orderDTO.getTokenNumber() != null) {
            order.setTokenNumber(orderDTO.getTokenNumber());
        }
        if (orderDTO.getTotal() != 0) {
            order.setTotal(orderDTO.getTotal());
        }

        // Update associated OrderGas items if provided
        if (orderDTO.getOrderGasList() != null) {
            for (OrderGasDTO orderGasDTO : orderDTO.getOrderGasList()) {
                OrderGas orderGas = orderGasRepository.findById(orderGasDTO.getId())
                        .orElseThrow(() -> new RuntimeException("OrderGas not found with id: " + orderGasDTO.getId()));
                if (orderGasDTO.getQuantity() != 0) {
                    orderGas.setQuantity(orderGasDTO.getQuantity());
                }
                orderGasRepository.save(orderGas);
            }
        }

        // Save the updated order
        Order updatedOrder = orderRepository.save(order);

        // Convert the updated order to a DTO and return it
        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setId(updatedOrder.getId());
        updatedOrderDTO.setStatus(updatedOrder.getStatus());
        updatedOrderDTO.setTokenNumber(updatedOrder.getTokenNumber());
        updatedOrderDTO.setUserId(updatedOrder.getUser().getId());
        updatedOrderDTO.setDeliveryScheduleId(updatedOrder.getDeliverySchedule() != null ? updatedOrder.getDeliverySchedule().getId() : null);
        updatedOrderDTO.setOutletId(updatedOrder.getOutlet().getId());
        updatedOrderDTO.setOrderGasList(updatedOrder.getOrderGasList().stream()
                .map(orderGas -> new OrderGasDTO(orderGas.getId(), orderGas.getGas().getId(), orderGas.getQuantity()))
                .collect(Collectors.toList()));

        return updatedOrderDTO;
    }
    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Delete associated OrderGas items first
        for (OrderGas orderGas : order.getOrderGasList()) {
            orderGasRepository.delete(orderGas);
        }

        orderRepository.delete(order);
    }

    public OrderDTO EntityToDTO(Order order) {
        if(order != null) {
            OrderDTO orderDTO = new OrderDTO();
            if (order.getId()!=0){
                orderDTO.setId(order.getId());
            }
            if (order.getStatus() != null) {
                orderDTO.setStatus(order.getStatus());
            }
            if (order.getTokenNumber() != null) {
                orderDTO.setTokenNumber(order.getTokenNumber());
            }
            if (order.getUser() != null) {
                orderDTO.setUserId(order.getUser().getId());
            }
            if (order.getDeliverySchedule() != null) {
                orderDTO.setDeliveryScheduleId(order.getDeliverySchedule().getId());
            }
            if (order.getOutlet() != null) {
                orderDTO.setOutletId(order.getOutlet().getId());
            }
            if (order.getOrderGasList() != null) {
                List<OrderGasDTO> orderGasDTOList = new ArrayList<>();
                for (OrderGas orderGas : order.getOrderGasList()) {
                    orderGasDTOList.add(new OrderGasDTO(orderGas.getId(), orderGas.getGas().getId(), orderGas.getQuantity()));
                }
                orderDTO.setOrderGasList(orderGasDTOList);
            }
            if(order.getTotal()!=0){
                orderDTO.setTotal(order.getTotal());
            }
            return orderDTO;
        }
        return null;
    }



    public Order DTOtoEntity(OrderDTO orderDTO) {
        if (orderDTO != null) {
            Order order = new Order();

            // Set ID
            if (orderDTO.getId() != 0) {
                order.setId(orderDTO.getId());
            }

            // Set status
            if (orderDTO.getStatus() != null) {
                order.setStatus(orderDTO.getStatus());
            }

            // Set token number
            if (orderDTO.getTokenNumber() != null) {
                order.setTokenNumber(orderDTO.getTokenNumber());
            }

            // Set user
            if (orderDTO.getUserId() != 0) {
                User user = new User(); // Create a new User entity
                user.setId(orderDTO.getUserId()); // Set the user ID from the DTO
                order.setUser(user);
            }

            // Set delivery schedule
            if (orderDTO.getDeliveryScheduleId() != null) {
                DeliverySchedule deliverySchedule = new DeliverySchedule(); // Create a new DeliverySchedule entity
                deliverySchedule.setId(orderDTO.getDeliveryScheduleId()); // Set the delivery schedule ID from the DTO
                order.setDeliverySchedule(deliverySchedule);
            }

            // Set outlet
            if (orderDTO.getOutletId() != null) {
                Outlet outlet = new Outlet(); // Create a new Outlet entity
                outlet.setId(orderDTO.getOutletId()); // Set the outlet ID from the DTO
                order.setOutlet(outlet);
            }

            // Set order gas list
            if (orderDTO.getOrderGasList() != null) {
                List<OrderGas> orderGasList = new ArrayList<>();
                for (OrderGasDTO orderGasDTO : orderDTO.getOrderGasList()) {
                    OrderGas orderGas = new OrderGas(); // Create a new OrderGas entity
                    orderGas.setId(orderGasDTO.getId());
                    Gas gas = new Gas(); // Create a new Gas entity
                    gas.setId(orderGasDTO.getGasId()); // Set the gas ID from the DTO
                    orderGas.setGas(gas);
                    orderGas.setQuantity(orderGasDTO.getQuantity());
                    orderGasList.add(orderGas);
                }
                order.setOrderGasList(orderGasList);
            }

            // Set total
            if (orderDTO.getTotal() != 0) {
                order.setTotal(orderDTO.getTotal());
            }

            return order;
        }
        return null;
    }

}
