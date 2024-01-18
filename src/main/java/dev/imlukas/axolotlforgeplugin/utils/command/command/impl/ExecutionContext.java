package dev.imlukas.axolotlforgeplugin.utils.command.command.impl;

import dev.imlukas.axolotlforgeplugin.utils.command.language.CompiledObjective;
import dev.imlukas.axolotlforgeplugin.utils.command.language.data.ObjectiveMetadata;

public abstract class ExecutionContext extends CompiledObjective { // Just a friendly rename

    public ExecutionContext(ObjectiveMetadata metadata) {
        super(metadata);
    }
}
