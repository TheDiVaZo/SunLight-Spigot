package su.nightexpress.sunlight.module.chat.command.pm;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.sunlight.SunLight;
import su.nightexpress.sunlight.module.chat.config.ChatPerms;
import su.nightexpress.sunlight.module.chat.config.ChatLang;

public class TellCommand extends PrivateMessageCommand {

    public static final String NAME = "tell";

    public TellCommand(@NotNull SunLight plugin, @NotNull String[] aliases) {
        super(plugin, aliases, ChatPerms.COMMAND_TELL, Type.INITIAL);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(ChatLang.COMMAND_TELL_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(ChatLang.COMMAND_TELL_DESC).getLocalized();
    }
}
