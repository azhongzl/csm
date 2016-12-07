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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itdoes.common.business.web.BaseController;
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
@RequestMapping("/admin/chat")
public class AdminChatController extends BaseController {
	@Autowired
	private ChatService chatService;

	private final SimpMessagingTemplate template;

	@Autowired
	public AdminChatController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@RequestMapping
	public String adminChat(Principal principal, Model model) {
		model.addAttribute("currentUserGroup", chatService.getUserGroup(getShiroUser(principal)));
		return "admin/chat";
	}

	@RequestMapping("icon")
	public String adminChatIcon(
			@RequestParam(value = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, required = false) String username,
			@RequestParam(value = "iconWidth", required = false) String iconWidth,
			@RequestParam(value = "iconHeight", required = false) String iconHeight, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("iconWidth", iconWidth);
		model.addAttribute("iconHeight", iconHeight);
		return "admin/chatIcon";
	}

	@RequestMapping("hasUnhandledCustomers")
	@ResponseBody
	public Result adminHasUnhandledCustomers(Principal principal) {
		return HttpResults.success(chatService.hasUnhandledCustomers(getShiroUser(principal)));
	}

	@RequestMapping(value = "addCustomerUserGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result adminAddCustomerUserGroup(@RequestParam("customerId") String customerId,
			@RequestParam("userGroupId") String userGroupId, Principal principal) {
		return HttpResults
				.success(chatService.addCustomerUserGroup(customerId, userGroupId, getShiroUser(principal), template));
	}

	@RequestMapping(value = "removeCustomerUserGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result adminRemoveCustomerUserGroup(@RequestParam("customerUserGroupId") String id,
			@RequestParam("customerId") String customerId, @RequestParam("userGroupId") String userGroupId) {
		chatService.removeCustomerUserGroup(id, customerId, userGroupId, template);
		return HttpResults.success();
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
