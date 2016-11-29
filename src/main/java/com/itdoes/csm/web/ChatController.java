package com.itdoes.csm.web;

import java.security.Principal;
import java.util.List;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itdoes.common.core.Result;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.csm.dto.ChatUser;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.service.ChatService;

/**
 * @author Jalen Zhong
 */
@Controller
public class ChatController {
	@Autowired
	private ChatService chatService;

	private final SimpMessagingTemplate template;

	@Autowired
	public ChatController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@RequestMapping("/chat")
	public String chat() {
		return "chat";
	}

	@RequestMapping("/admin/chat/icon")
	public String adminChatIcon(
			@RequestParam(value = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, required = false) String username,
			@RequestParam(value = "iconWidth", required = false) String iconWidth,
			@RequestParam(value = "iconHeight", required = false) String iconHeight, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("iconWidth", iconWidth);
		model.addAttribute("iconHeight", iconHeight);
		return "admin/chatIcon";
	}

	@RequestMapping("/admin/chat")
	public String adminChat() {
		return "admin/chat";
	}

	@RequestMapping("/admin/chat/hasUnhandledCustomers")
	@ResponseBody
	public Result hasUnhandledCustomers(Principal principal) {
		return HttpResults.success(chatService.hasUnhandledCustomers(getShiroUser(principal)));
	}

	@RequestMapping("/jalenChat")
	public String jalenChat() {
		return "jalenChat";
	}

	@RequestMapping("/admin/jalenChat")
	public String jalenAdminChat() {
		return "admin/jalenChat";
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<CsmChatMessage> chatCInitMessage(Principal principal) {
		return chatService.customerInitMessage(getShiroUser(principal));
	}

	@MessageMapping("/chatCSendMessage")
	public void chatCSendMessage(CsmChatMessage message, Principal principal) {
		chatService.customerSendMessage(message, getShiroUser(principal), template);
	}

	@SubscribeMapping("/chatAInit")
	public List<ChatUser> chatAInit(Principal principal) {
		return chatService.adminInit(getShiroUser(principal));
	}

	@SubscribeMapping("/chatAInitMessage/{roomId}")
	public List<CsmChatMessage> chatAInitMessage(@DestinationVariable String roomId, Principal principal) {
		return chatService.adminInitMessage(roomId, getShiroUser(principal));
	}

	@MessageMapping("/chatASendMessage")
	public void chatASendMessage(CsmChatMessage message, Principal principal) {
		chatService.adminSendMessage(message, getShiroUser(principal), template);
	}

	private ShiroUser getShiroUser(Principal principal) {
		return Shiros.getShiroUser(principal);
	}
}
