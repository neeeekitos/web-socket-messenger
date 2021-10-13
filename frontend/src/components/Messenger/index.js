import React, {useEffect, useState} from 'react';
import ConversationList from '../ConversationList';
import MessageList from '../MessageList';
import './Messenger.css';
import axios from "axios";
import { Client } from '@stomp/stompjs';
import {Button} from "react-bootstrap";
import styles from "../ConversationList/addchat.module.css";

export default function Messenger(props) {

    const [allUsers, setAllUsers] = useState([]);
    const [chats, setChats] = useState([]);
    const [currentChatId, setCurrentChatId] = useState(-1);
    const [messages, setMessages] = useState([]);
    const [authenticated, setAuth] = useState(false);
    const [username, setUsername] = useState("");


    useEffect(async () => {
        connect();
    },[])

    const connect = () => {

        let client = new Client();

        client.configure({
            brokerURL: 'ws://localhost:8081/stomp1',
            onConnect: async () => {
                console.log('onConnect');

                client.subscribe('/topic/public', message => onAllUsersReceived(message));
                client.subscribe('/topic/chats', message => onAllChatsReceived(message));
                client.subscribe('/topic/messages', message => onAllMessagesReceived(message));
                client.subscribe('/topic/users', message => onAllUsersReceived(message));
                client.subscribe('/topic/newMessage', message => onNewMessageReceived(message));
                if (await isAuthenticated())
                    await afterAuth(); // fetch users and chats
            },
            // Helps during debugging, remove in production
            debug: (str) => {
                console.log(new Date(), str);
            }
        });

        client.activate();
    }

    const getMessagesByChatId = (chatId) => {
        axios.post(`http://localhost:8081/actions/changeCurrentChat/${chatId}`, null, {
            headers: {
                'Content-Type': 'application/json'
            },
            responseType: 'text'
        }).then(response => {
            console.log(response);
            axios.get(`http://localhost:8081/messages/getMessagesByChatId?chatId=${chatId}`).then(console.log);

        });
    }

    const onAllUsersReceived = (message) => {
        console.log("Users subscription response :");
        if (message.body) {
            const users = JSON.parse(message.body);
            setAllUsers(users);
            console.log(users);
        } else {
            console.log("got empty message");
        }
    }

    const isAuthenticated = async () => {
        const response = await axios.get("http://localhost:8081/auth/isAuthenticated");
        const isAuth = response.data;
        console.log(isAuth ? "User is authenticated" : "User is not authenticated");
        if (isAuth) {
            setAuth(true);
            const usernameResponse = await axios.get("http://localhost:8081/auth/getUsername");
            setUsername(usernameResponse.data);
        }
        return isAuth;
    }

    const authenticate = async () => {
        const response = await axios.post('http://localhost:8081/auth/authenticate', username,
            {
                headers: {
                    'Content-Type': 'text/plain',
                }
            });
        console.log(response.data, response.status);
        setAuth(true);
        await afterAuth();
    }

    const afterAuth = async () => {
        console.log("fetching user infos...")
        await axios.get("http://localhost:8081/actions/allUsers");
        await axios.get("http://localhost:8081/actions/allUserChats");
    }

    const onAllChatsReceived = (message) => {
        console.log("Chats subscription response :");
        if (message.body) {
            console.log(JSON.parse(message.body))
            const chatsResponse = Object.values(JSON.parse(message.body));
            setChats(chatsResponse);
            if (chatsResponse.length > 0) {
                setCurrentChatId(chatsResponse[0].chatId);
                // axios.get(`http://localhost:8081/messages/getMessagesByChatId?chatId=${currentChatId}`).then();
            }
            console.log(chatsResponse);
        } else {
            console.log("got empty message");
        }
    }

    const onAllMessagesReceived = async (message) => {
        console.log("Messages subscription response :");
        if (message.body) {
            const msgsResponse = Object.values(JSON.parse(message.body));
            const orderedMessages = msgsResponse.reverse().map(msg => processMessage(msg));
            setMessages(orderedMessages);
            console.log(msgsResponse);
        } else {
            console.log("got empty message");
        }
    }

    const onNewMessageReceived = async (message) => {
        console.log("New message subscription response :");
        if (message.body) {
            // console.log(JSON.parse(message.body))
            const msgResponse = Object.values(JSON.parse(message.body))[5];
            console.log("messages : " + messages);

            setMessages(messages => [...messages, processMessage(msgResponse)]);
            console.log(JSON.stringify(messages))

        } else {
            console.log("got empty message");
        }
    }

    const processMessage = (message) => {
        console.log("puto :" + message)
        return {
            id: message.id,
            author: message.sender.username,
            message: message.text,
            timestamp: new Date(message.time)
        }
    }

    const onTestReceived = (payload) => {
        console.log("test received");
        var message = JSON.parse(payload.body);
        console.log(message);
    }

    const sendMessage = (message) => {
        console.log("sending message : "+ message + " from : " + username)
        axios.post("http://localhost:8081/messages/sendMessage", message, {
            headers: {
                'Content-Type': 'text/plain'
            },
            responseType: 'text'
        }).then(console.log);
    }


    return (
        <div className="messenger">

            {/* <Toolbar
          title="Messenger"
          leftItems={[
            <ToolbarButton key="cog" icon="ion-ios-cog" />
          ]}
          rightItems={[
            <ToolbarButton key="add" icon="ion-ios-add-circle-outline" />
          ]}
        /> */}

            {/* <Toolbar
          title="Conversation Title"
          rightItems={[
            <ToolbarButton key="info" icon="ion-ios-information-circle-outline" />,
            <ToolbarButton key="video" icon="ion-ios-videocam" />,
            <ToolbarButton key="phone" icon="ion-ios-call" />
          ]}
        /> */}
            <div className="scrollable sidebar">
                {/*<button onClick={async () => {*/}
                {/*    setAuth(true);*/}
                {/*    await afterAuth();*/}
                {/*}}>Authenticated?</button>*/}

                <ConversationList username={username} getMessagesByChatId={getMessagesByChatId} allUsers={allUsers} chats={chats}/>
            </div>
            <div hidden={authenticated} className="authenticationPopupBox">
                <div className="authenticationBox">
                    <h3>Authentication</h3>
                    <input
                        type="text"
                        list="data" // link to datalist below
                        className="conversation-search-input"
                        placeholder="Your username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />
                    <Button className="btn" onClick={() => authenticate()}>Login</Button>
                </div>
            </div>

            <div className="scrollable content">
              <MessageList username={username} sendMessage={sendMessage} messagesProp={messages}/>
            </div>
        </div>
    );
}