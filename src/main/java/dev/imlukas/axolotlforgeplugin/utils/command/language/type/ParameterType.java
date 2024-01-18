package dev.imlukas.axolotlforgeplugin.utils.command.language.type;

public interface ParameterType<Type> {

    boolean isType(String input);

    Type parse(String input);

    Type getDefaultValue();

}
