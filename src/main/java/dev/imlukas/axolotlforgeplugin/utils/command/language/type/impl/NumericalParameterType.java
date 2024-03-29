package dev.imlukas.axolotlforgeplugin.utils.command.language.type.impl;

import dev.imlukas.axolotlforgeplugin.utils.command.language.type.ParameterType;


public class NumericalParameterType implements ParameterType<Double> {

    @Override
    public boolean isType(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Double parse(String input) {
        return Double.parseDouble(input);
    }

    @Override
    public Double getDefaultValue() {
        return 0d;
    }
}
