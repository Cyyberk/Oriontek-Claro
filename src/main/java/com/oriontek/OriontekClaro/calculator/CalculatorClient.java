package com.oriontek.OriontekClaro.calculator;

import org.springframework.stereotype.Component;
import org.tempuri.Calculator;
import org.tempuri.CalculatorSoap;

@Component
public class CalculatorClient {
    
    // Calculadora traida desde un servicio SOAP externo.
    private final CalculatorSoap calculatorSoap;

    public CalculatorClient() {
        this.calculatorSoap = new Calculator().getCalculatorSoap();
    }

    public int add(int a, int b){
        return calculatorSoap.add(a,b);
    }
}
