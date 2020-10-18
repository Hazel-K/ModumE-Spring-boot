var sectionContainer = document.querySelector('.sectionContainer');
var centralContainer = document.querySelector('.centralContainer');


var stompClient = null;
var inputChat = null;
var username = null;
var inputUl = null;

function chatInit() {
    const chatContainer = document.querySelector('.chatContainer');
    const boardContainer = document.querySelector('.boardContainer');
    if(boardContainer != null) {
        closeContainer(boardContainer);
    }
    if(chatContainer == null) {
        openChat();
    } else {
        closeContainer(chatContainer);
    }
}

function boardInit() {
    const boardContainer = document.querySelector('.boardContainer');
    const chatContainer = document.querySelector('.chatContainer');
    if(chatContainer != null) {
        closeContainer(chatContainer);
    } 
    if(boardContainer == null) {
        openboard();
    } else {
        closeContainer(boardContainer);
    }
}

function openChat() {
    const makeDiv = document.createElement('div');
    makeDiv.className = 'chatContainer';
    sectionContainer.append(makeDiv);
	/*채팅관련 창들 */
	const chatDiv = document.createElement('div');
	chatDiv.className = 'hidden';``
	makeDiv.append(chatDiv);
	inputChat = document.createElement('input');
	inputChat.className = 'inputChat';
	makeDiv.append(inputChat);
	inputUl = document.createElement('ul');
	inputUl.className = 'messageArea';
	chatDiv.append(inputUl);
	const btnChat = document.createElement('button');
	btnChat.className = 'btnChat';
	makeDiv.append(btnChat);
	btnChat.innerText = "보내기";
	btnChat.addEventListener('click', sendMessage, true);
	connect()
	console.log('chat화면 띄우기 완료')
}

function openboard() {
    const makeDiv = document.createElement('div');
    makeDiv.className = 'boardContainer';
    sectionContainer.append(makeDiv);
}

function closeContainer(ele) {
    ele.remove();
}

function onConnected(){
	console.log("I don't know")
	stompClient.subscribe('/topic/public', onMessageReceived);
	stompClient.send("/app/chat.addUser",
	{},
	JSON.stringify({sender: username, type: 'JOIN'})
	)
	console.log("onConnected 끝")
}

function onError(error){
	connectingElement.textContent = "ConnectionError";
	connectingElement.style.color = 'red';
}

function sendMessage(event){
	console.log('message전송 시작')
	var messageContent = inputChat.value.trim();
	console.log('messageContent: ' + messageContent)
	if(messageContent && stompClient){
		var chatMessage = {
			sender : username,
			content: inputChat.value,
			type : 'CHAT'
		};
	stompClient.send("/app/chat.sendMessage",{},JSON.stringify(chatMessage));
	inputChat.value ='';
	}
	event.preventDefault();
}

function onMessageReceived(payload){
	var message = JSON.parse(payload.body);
	
	var messageElement = document.createElement('li');
	console.log('message.type:' + message.type)
	console.log('message.sender: ' + message.sender)
	
	if(message.type === "JOIN"){
		message.content = message.sender + "joined!";
	}else if(message.type === "LEVAE"){
		message.content = message.sender + "left!";
	}else{
		/* 채팅창에 채팅 메세지 띄우기  message.type == "CHAT" */
		messageElement.className='chat-message';
		inputUl.append(messageElement);
		

		/*var avatarElement = document.createElement('i');
		var avatarText = document.createElement(message.sender[0]);
		avatarElement.appendChild(avatarText);
		avatarElement.style['background-color'] = getAvatarColor(message.sender);*/
		
		
		/*messageElement.appendChild(avatarElement);*/
		
		

		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);	
	}
	
	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);
	
	messageElement.appendChild(textElement);
}

/*function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}*/

/*usernameForm.addEventListener('submit', connect, true)*/
