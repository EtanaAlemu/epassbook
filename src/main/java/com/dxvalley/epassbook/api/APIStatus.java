package com.dxvalley.epassbook.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class APIStatus {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonIgnore
        private Long ApiId;
        private Boolean atmCardEnabled;
        private Boolean accountStatementEnabled;
        private Boolean cardLessCashEnabled;
        private Boolean atmPinChangeEnabled;
        private Boolean newProductEnabled;
        private Boolean earlyPayDayEnabled;

        public APIStatus(Boolean atmCardEnabled, Boolean accountStatementEnabled, Boolean cardLessCashEnabled,
                         Boolean atmPinChangeEnabled, Boolean newProductEnabled, Boolean earlyPayDayEnabled) {
                this.atmCardEnabled = atmCardEnabled;
                this.accountStatementEnabled = accountStatementEnabled;
                this.cardLessCashEnabled = cardLessCashEnabled;
                this.atmPinChangeEnabled = atmPinChangeEnabled;
                this.newProductEnabled = newProductEnabled;
                this.earlyPayDayEnabled = earlyPayDayEnabled;
        }

}
