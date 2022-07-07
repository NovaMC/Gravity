package xyz.novaserver.gravity.command;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.novaserver.gravity.Gravity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SendCommand implements SimpleCommand {

    ProxyServer proxy = Gravity.getInstance().getProxy();
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length == 0) return;

        if(args.length > 1 && args[0].equalsIgnoreCase("all")){
            Optional<RegisteredServer> toConnect = proxy.getServer(args[1]);
            if(toConnect.isPresent()) {
                proxy.getAllPlayers().forEach(player -> {
                    player.createConnectionRequest(toConnect.get()).fireAndForget();
                    player.sendMessage(Component.text("You have been sent to "
                            + toConnect.get().getServerInfo().getName() + ".").color(NamedTextColor.GREEN));
                });
                source.sendMessage(Component.text("Sent " + proxy.getAllPlayers().size()
                        + " players to " + toConnect.get().getServerInfo().getName() + ".").color(NamedTextColor.YELLOW));
            }
        }
        else if(args.length > 2) {
            if(args[0].equalsIgnoreCase("player")){
                Optional<RegisteredServer> toConnect = proxy.getServer(args[2]);
                Optional<Player> player = proxy.getPlayer(args[1]);
                if(player.isPresent() && toConnect.isPresent()) {
                    player.get().createConnectionRequest(toConnect.get()).fireAndForget();
                    source.sendMessage(Component.text("Sent " + player.get().getUsername()
                            + " to " + toConnect.get().getServerInfo().getName() + ".").color(NamedTextColor.YELLOW));
                    player.get().sendMessage(Component.text("You have been sent to "
                            + toConnect.get().getServerInfo().getName() + ".").color(NamedTextColor.GREEN));
                }
            }
            else if(args[0].equalsIgnoreCase("server")){
                Optional<RegisteredServer> toConnect = proxy.getServer(args[2]);
                Optional<RegisteredServer> fromServer = proxy.getServer(args[1]);
                if(toConnect.isPresent() && fromServer.isPresent()) {
                    fromServer.get().getPlayersConnected().forEach(player -> {
                        player.createConnectionRequest(toConnect.get()).fireAndForget();
                        player.sendMessage(Component.text("You have been sent to "
                                + toConnect.get().getServerInfo().getName() + ".").color(NamedTextColor.GREEN));
                    });
                    source.sendMessage(Component.text("Sent players from " + fromServer.get().getServerInfo().getName()
                            + " to " + toConnect.get().getServerInfo().getName() + ".").color(NamedTextColor.YELLOW));
                }
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        Stream<String> possibilities = Stream.of("all", "player", "server");
        String[] args = invocation.arguments();
        Stream<String> servers = proxy.getAllServers().stream().map(server -> server.getServerInfo().getName());

        if (args.length == 0) {
            return possibilities.collect(Collectors.toUnmodifiableList());
        }
        else if (args.length == 1) {
            return getMatchingArgs(possibilities, args[0]);
        }
        else if(args.length == 2){
            if(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("server")){
                return getMatchingArgs(servers, args[1]);
            }
            else if (args[0].equalsIgnoreCase("player")){
                Stream<String> players = proxy.getAllPlayers().stream().map(Player::getUsername);
                return getMatchingArgs(players, args[1]);
            }
        }
        else if(args.length == 3){
            if(args[0].equalsIgnoreCase("player") || args[0].equalsIgnoreCase("server")){
                return getMatchingArgs(servers, args[2]);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("gravity.command.send");
    }

    private List<String> getMatchingArgs(Stream<String> possibilities, String argToMatch){
        return possibilities
                .filter(name -> name.regionMatches(true, 0, argToMatch, 0, argToMatch.length()))
                .collect(Collectors.toUnmodifiableList());
    }
}
