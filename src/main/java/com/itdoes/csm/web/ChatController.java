package com.itdoes.csm.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.web.BaseController;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.MediaTypes;
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

	@RequestMapping(value = "listHistory", method = RequestMethod.GET, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result listHistory(@RequestParam(value = "beginDateTime", required = false) LocalDateTime beginDateTime,
			@RequestParam(value = "endDateTime", required = false) LocalDateTime endDateTime,
			@RequestParam(value = PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = PAGE_SIZE, defaultValue = "-1") int pageSize, Principal principal) {
		return chatService.listHistory(beginDateTime, endDateTime, pageNo, pageSize, principal);
	}

	@SubscribeMapping("/chatCInitMessage")
	public Result initMessage(Principal principal) {
		return chatService.initMessage(principal);
	}

	@MessageMapping("/chatCSendMessage")
	public void sendMessage(CsmChatMessage message, Principal principal) {
		chatService.sendMessage(message, principal, template);
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaTypes.APPLICATION_JSON_UTF_8)
	@ResponseBody
	public Result upload(@Valid CsmChatMessage message, @RequestParam(UPLOAD_FILE) List<MultipartFile> uploadFileList,
			Principal principal) {
		return chatService.upload(message, uploadFileList, principal, template);
	}
}
