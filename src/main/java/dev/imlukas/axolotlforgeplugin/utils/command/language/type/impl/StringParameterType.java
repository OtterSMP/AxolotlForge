package dev.imlukas.axolotlforgeplugin.utils.command.language.type.impl;

import dev.imlukas.axolotlforgeplugin.utils.command.language.type.ParameterType;


public class StringParameterType implements ParameterType<String> {

    @Override
    public boolean isType(String input) {
        return true;
    }

    @Override
    public String parse(String input) {
        return input;
    }

    @Override
    public String getDefaultValue() {
        return "";
    }
}
