package xyz.pwncraft.ftbextras.commands.team.admin;

import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import xyz.pwncraft.ftbextras.FTBLibCompat;
import xyz.pwncraft.ftbextras.utility.Utility;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class TransferOwnerExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<User> user = args.getOne("player");
        Optional<ForgePlayer> player = user
                .map(User::getUniqueId)
                .map(Universe.get()::getPlayer);
        if (!player.isPresent()) throw new CommandException(Utility.format("&f[&6FTBExtras&r] Player not found!"));
        ForgeTeam team = player.get().team;
        if (!team.isValid()) throw new CommandException (Utility.format("[&6FTBExtras&r] Player " + player.get().getName() + " is not part of a team!"));

        ForgePlayer p = Universe.get().getPlayer(player.get().getId());
        if (!player.get().hasTeam()) throw new CommandException (Utility.format("&f[&6FTBExtras&r] Player " + player.get().getName() + " has no team!"));

        String teamId = null;
        try {
            teamId = FTBLibCompat.getInstance().getId(team);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (team.setStatus(p, EnumTeamStatus.OWNER)) {
            src.sendMessage(Utility.format("[&6FTBExtras&r] Changed owner of team " + teamId + " to " + player.get().getName()));
        } else {
            src.sendMessage(Utility.format("[&6FTBExtras&r] Failed to change owner of team " + teamId));
        }



        return CommandResult.success();
    }
}
