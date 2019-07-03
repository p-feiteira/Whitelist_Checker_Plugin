
import com.velocitypowered.api.proxy.ProxyServer;

import config.Configuration;
import config.PluginInfo;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import java.io.IOException;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION)
public class IPHandler {
	
	private static final Component NOT_ALLOWED = TextComponent
		      .of("You do not belong here", TextColor.RED);

	private final ProxyServer proxy;
	private final Logger logger;
	private Configuration configuration = null;

	@Inject
	public IPHandler(ProxyServer proxy, Logger logger) {
		this.proxy = proxy;
		this.logger = logger;
		try {
			configuration = new Configuration(logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("IP Filter started");
	}
	
	 @Subscribe
	    public void onPreLogin(PreLoginEvent event) {
		 	String username = event.getUsername();
		 	String playerEventIP = event.getConnection().getRemoteAddress().getHostName();
		 	System.out.println("Username: " + username);
		 	System.out.println("IP: " + playerEventIP);
			String playerIP = configuration.isInWhiteList(username);
			if(playerIP == null) {
				logger.info("{} Not allowed!", playerIP);
				 event.setResult(PreLoginComponentResult.denied(NOT_ALLOWED));
			}else {
				System.out.println("IP at DB: " + playerIP);
				if(!playerIP.equals(playerEventIP)) {
					configuration.updateWhiteList(username, playerEventIP);
				}
			}
	    }
	
	 @Subscribe
	 public void onExit(ProxyShutdownEvent event) {
			logger.info("Turning off plugin..");
			try {
				configuration.store();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
}

