package com.itdoes.csm.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.shiro.ShiroUser;
import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.service.ChatService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/chat")
public class ChatController extends BaseController {
	@Autowired
	private ChatService chatService;

	private final SimpMessagingTemplate template;

	@Autowired
	public ChatController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@RequestMapping
	public String chat() {
		return "chat";
	}

	@SubscribeMapping("/chatCInitMessage")
	public List<CsmChatMessage> chatCInitMessage(Principal principal) {
		return chatService.customerInitMessage(getShiroUser(principal));
	}

	@MessageMapping("/chatCSendMessage")
	public void chatCSendMessage(CsmChatMessage message, Principal principal) {
		chatService.customerSendMessage(message, getShiroUser(principal), template);
	}

	private ShiroUser getShiroUser(Principal principal) {
		return Shiros.getShiroUser(principal);
	}
}
