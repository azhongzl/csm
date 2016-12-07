package com.itdoes.csm.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.csm.entity.CsmChatMessage;
import com.itdoes.csm.service.ui.ChatUiService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/chat")
public class ChatController extends BaseController {
	@Autowired
	private ChatUiService chatService;

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
	public Result customerInitMessage(Principal principal) {
		return chatService.initMessage(principal);
	}

	@MessageMapping("/chatCSendMessage")
	public void sendMessage(CsmChatMessage message, Principal principal) {
		chatService.customerSendMessage(message, principal, template);
	}
}
