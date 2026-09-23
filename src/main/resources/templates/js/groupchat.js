"use strict";


/* =========================================================
   CURRENT USER
   ========================================================= */

/*
    Change this value to:

    "ADMIN"  → Admin can send broadcast messages
    "CLIENT" → Client can only receive messages
*/

const CURRENT_USER_ROLE = "CLIENT";


const currentUser = {

    id: 1,

    name: "Sayan",

    role: CURRENT_USER_ROLE

};


/* =========================================================
   DOM ELEMENTS
   ========================================================= */

const messagesArea =
    document.getElementById("messagesArea");

const messageInput =
    document.getElementById("messageInput");

const sendButton =
    document.getElementById("sendButton");

const emojiButton =
    document.getElementById("emojiButton");

const emojiPanel =
    document.getElementById("emojiPanel");

const attachmentButton =
    document.getElementById("attachmentButton");

const fileInput =
    document.getElementById("fileInput");

const memberSearch =
    document.getElementById("memberSearch");

const unreadBadge =
    document.getElementById("unreadBadge");

const groupLastMessage =
    document.getElementById("groupLastMessage");

const groupTime =
    document.getElementById("groupTime");

const profileName =
    document.getElementById("profileName");

const profileRole =
    document.getElementById("profileRole");

const profileAvatar =
    document.getElementById("profileAvatar");

const messageInputContainer =
    document.getElementById("messageInputContainer");

const clientReadonlyMessage =
    document.getElementById("clientReadonlyMessage");


/* =========================================================
   CONFIGURE USER UI
   ========================================================= */

function configureUserInterface() {

    profileName.textContent =
        currentUser.name;

    profileRole.textContent =
        currentUser.role;

    profileAvatar.textContent =
        getInitial(currentUser.name);


    /*
        CLIENT:
        Cannot send public group messages.
    */

    if (currentUser.role === "CLIENT") {

        messageInputContainer.style.display = "none";

        clientReadonlyMessage.classList.add("active");

    }


    /*
        ADMIN:
        Can send public group messages.
    */

    else if (currentUser.role === "ADMIN") {

        messageInputContainer.style.display = "flex";

        clientReadonlyMessage.classList.remove("active");

    }

}


/* =========================================================
   SEND MESSAGE
   ========================================================= */

function sendMessage() {


    /*
        FRONTEND VALIDATION

        Only ADMIN can send public-group messages.
    */

    if (currentUser.role !== "ADMIN") {

        alert(
            "Only ADMIN can send broadcast messages."
        );

        return;
    }


    const message =
        messageInput.value.trim();


    /*
        Do not send empty message.
    */

    if (!message) {
        return;
    }


    const time =
        getCurrentTime();


    /*
        Message object.

        Later this object can be sent
        through STOMP/WebSocket.
    */

    const messageData = {

        id: Date.now(),

        senderId: currentUser.id,

        senderName: currentUser.name,

        role: currentUser.role,

        content: message,

        time: time

    };


    /*
        Currently we only display
        the message in the UI.
    */

    addMessageToUI(messageData);


    /*
        Clear input.
    */

    messageInput.value = "";


    /*
        Update group preview.
    */

    groupLastMessage.textContent =
        "You: " + message;


    /*
        Update time.
    */

    groupTime.textContent =
        time;


    /*
        Remove unread count.
    */

    unreadBadge.style.display = "none";


    /*
        Scroll to newest message.
    */

    scrollToBottom();

}


/* =========================================================
   ADD MESSAGE TO UI
   ========================================================= */

function addMessageToUI(message) {


    /*
        Check whether this message
        belongs to current user.
    */

    const isOwnMessage =
        message.senderId === currentUser.id;


    const messageRow =
        document.createElement("div");


    messageRow.classList.add(
        "message-row"
    );


    if (isOwnMessage) {

        messageRow.classList.add(
            "sent"
        );

    } else {

        messageRow.classList.add(
            "received"
        );

    }


    /*
        RECEIVED MESSAGE

        Show sender avatar.
    */

    if (!isOwnMessage) {

        const avatar =
            document.createElement("div");


        avatar.classList.add(
            "message-avatar"
        );


        if (message.role === "ADMIN") {

            avatar.classList.add(
                "admin-avatar"
            );

        }


        avatar.textContent =
            getInitial(message.senderName);


        messageRow.appendChild(
            avatar
        );

    }


    /*
        MESSAGE BUBBLE
    */

    const bubble =
        document.createElement("div");


    bubble.classList.add(
        "message-bubble"
    );


    if (isOwnMessage) {

        bubble.classList.add(
            "own-message"
        );

    }


    if (message.role === "ADMIN") {

        bubble.classList.add(
            "admin-message"
        );

    }


    /*
        Sender name

        Do not show current user's
        name inside own message.
    */

    if (!isOwnMessage) {

        const senderName =
            document.createElement("div");


        senderName.classList.add(
            "sender-name"
        );


        if (message.role === "ADMIN") {

            senderName.classList.add(
                "admin-name"
            );

        }


        senderName.textContent =
            message.senderName;


        bubble.appendChild(
            senderName
        );

    }


    /*
        Message text
    */

    const messageText =
        document.createElement("div");


    messageText.classList.add(
        "message-text"
    );


    messageText.textContent =
        message.content;


    bubble.appendChild(
        messageText
    );


    /*
        Message metadata
    */

    const messageMeta =
        document.createElement("div");


    messageMeta.classList.add(
        "message-meta"
    );


    const time =
        document.createElement("span");


    time.textContent =
        message.time;


    messageMeta.appendChild(
        time
    );


    /*
        Double tick for own messages.
    */

    if (isOwnMessage) {

        const status =
            document.createElement("span");


        status.classList.add(
            "message-status"
        );


        status.textContent =
            "✓✓";


        messageMeta.appendChild(
            status
        );

    }


    bubble.appendChild(
        messageMeta
    );


    messageRow.appendChild(
        bubble
    );


    messagesArea.appendChild(
        messageRow
    );

}


/* =========================================================
   SYSTEM MESSAGE
   ========================================================= */

function addSystemMessage(text) {


    const systemMessage =
        document.createElement("div");


    systemMessage.classList.add(
        "system-message"
    );


    const span =
        document.createElement("span");


    span.textContent =
        text;


    systemMessage.appendChild(
        span
    );


    messagesArea.appendChild(
        systemMessage
    );


    scrollToBottom();

}


/* =========================================================
   CURRENT TIME
   ========================================================= */

function getCurrentTime() {

    const now =
        new Date();


    return now.toLocaleTimeString(
        [],
        {
            hour: "2-digit",
            minute: "2-digit"
        }
    );

}


/* =========================================================
   GET INITIAL
   ========================================================= */

function getInitial(name) {

    if (!name) {
        return "?";
    }


    return name
        .charAt(0)
        .toUpperCase();

}


/* =========================================================
   SCROLL
   ========================================================= */

function scrollToBottom() {

    messagesArea.scrollTop =
        messagesArea.scrollHeight;

}


/* =========================================================
   SEND BUTTON
   ========================================================= */

sendButton.addEventListener(
    "click",
    sendMessage
);


/* =========================================================
   ENTER KEY
   ========================================================= */

messageInput.addEventListener(
    "keydown",
    function (event) {

        if (
            event.key === "Enter" &&
            !event.shiftKey
        ) {

            event.preventDefault();

            sendMessage();

        }

    }
);


/* =========================================================
   EMOJI BUTTON
   ========================================================= */

emojiButton.addEventListener(
    "click",
    function () {

        emojiPanel.classList.toggle(
            "active"
        );

    }
);


/* =========================================================
   EMOJI SELECTION
   ========================================================= */

emojiPanel
    .querySelectorAll("button")
    .forEach(
        function (button) {

            button.addEventListener(
                "click",
                function () {

                    /*
                        CLIENT cannot use
                        public-group message input.
                    */

                    if (
                        currentUser.role !== "ADMIN"
                    ) {

                        return;

                    }


                    messageInput.value +=
                        button.textContent;


                    messageInput.focus();

                }
            );

        }
    );


/* =========================================================
   CLOSE EMOJI PANEL
   ========================================================= */

document.addEventListener(
    "click",
    function (event) {

        if (
            !emojiPanel.contains(event.target) &&
            !emojiButton.contains(event.target)
        ) {

            emojiPanel.classList.remove(
                "active"
            );

        }

    }
);


/* =========================================================
   ATTACHMENT
   ========================================================= */

attachmentButton.addEventListener(
    "click",
    function () {


        if (currentUser.role !== "ADMIN") {

            return;

        }


        fileInput.click();

    }
);


/* =========================================================
   FILE SELECTION
   ========================================================= */

fileInput.addEventListener(
    "change",
    function () {

        const file =
            fileInput.files[0];


        if (!file) {
            return;
        }


        addSystemMessage(
            "Selected attachment: " +
            file.name
        );


        fileInput.value = "";

    }
);


/* =========================================================
   MEMBER SEARCH
   ========================================================= */

memberSearch.addEventListener(
    "input",
    function () {

        const searchText =
            memberSearch.value
                .trim()
                .toLowerCase();


        const members =
            document.querySelectorAll(
                ".member-item"
            );


        members.forEach(
            function (member) {

                const name =
                    member.dataset.name
                        .toLowerCase();


                if (
                    name.includes(searchText)
                ) {

                    member.style.display =
                        "flex";

                } else {

                    member.style.display =
                        "none";

                }

            }
        );

    }
);


/* =========================================================
   GROUP MENU
   ========================================================= */

document
    .getElementById("groupMenuButton")
    .addEventListener(
        "click",
        function () {

            alert(
                "Group menu will be connected later."
            );

        }
    );


/* =========================================================
   SEARCH MESSAGES
   ========================================================= */

document
    .getElementById("searchMessageButton")
    .addEventListener(
        "click",
        function () {

            const search =
                prompt(
                    "Search messages:"
                );


            if (!search) {
                return;
            }


            const messages =
                document.querySelectorAll(
                    ".message-text"
                );


            let found = false;


            messages.forEach(
                function (message) {

                    if (
                        message.textContent
                            .toLowerCase()
                            .includes(
                                search.toLowerCase()
                            )
                    ) {

                        message.scrollIntoView(
                            {
                                behavior: "smooth",
                                block: "center"
                            }
                        );


                        found = true;

                    }

                }
            );


            if (!found) {

                alert(
                    "Message not found."
                );

            }

        }
    );


/* =========================================================
   NEW GROUP
   ========================================================= */

document
    .getElementById("newGroupButton")
    .addEventListener(
        "click",
        function () {

            alert(
                "Group creation will be connected later."
            );

        }
    );


/* =========================================================
   MENU
   ========================================================= */

document
    .getElementById("menuButton")
    .addEventListener(
        "click",
        function () {

            alert(
                "Menu will be connected later."
            );

        }
    );


/* =========================================================
   INITIALIZE
   ========================================================= */

configureUserInterface();

scrollToBottom();