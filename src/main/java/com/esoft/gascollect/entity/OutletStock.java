    package com.esoft.gascollect.entity;

    import jakarta.persistence.Embeddable;
    import jakarta.persistence.Id;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class OutletStock {
        private int gasId; // Gas ID
        private int quantity; // Stock quantity available for this gas type
    }
