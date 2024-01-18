package dev.imlukas.axolotlforgeplugin.utils.command.language.type.impl;

import dev.imlukas.axolotlforgeplugin.utils.command.language.type.ParameterType;
import dev.imlukas.axolotlforgeplugin.utils.text.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface FilteredParameterType<Type> extends ParameterType<Type> {

    Collection<String> getSuggestions();

    @Nullable
    List<Placeholder<Player>> createPlaceholders(Object value);

}
