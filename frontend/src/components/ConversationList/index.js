import React, {useState, useEffect} from 'react';
import ConversationSearch from '../ConversationSearch';
import ConversationListItem from '../ConversationListItem';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import axios from 'axios';

import './ConversationList.css';
import AddChat from "./AddChat";

export default function ConversationList(props) {
  const [conversations, setConversations] = useState([]);
  const [addChatOpened, setNewChatOpen] = useState(false);

  useEffect(async () => {
    //await getChats()
  },[])

  const getChats = async () => {
      const response = await axios.get('http://localhost:8081/actions/allUserChats');
      const responseArray = Object.values(response.data);
        console.log(responseArray);
      const conversationsNumber = responseArray.length;
      console.log(`fetching images for ${conversationsNumber} conversations.`);

      const responseImagers = await axios.get(`https://randomuser.me/api/?results=${conversationsNumber}`);

      let newConversations = responseArray.map((result, index) => {
          return {
              photo: responseImagers.data.results[index].picture.large,
              name: `${result.groupName}`,
              text: 'Hello world! This is a long message that needs to be truncated.'
          };
      });
      // setConversations([...conversations, ...newConversations])
      setConversations(newConversations);
      console.log(response);
  }

  const createChat = async (isGroup, name) => {

      console.log('creating chat...')
      console.log(isGroup)
      console.log(name)
      let response;
      if (isGroup)
        response = await axios.post('http://localhost:8081/actions/createGroup', name,
            {
                headers: {
                    'Content-Type': 'text/plain',
                }
            });
      else
        response = await axios.post('http://localhost:8081/actions/createO2o', name,
            {
                headers: {
                    'Content-Type': 'text/plain',
                }
            });

      console.log(response.data, response.status);
      alert(`Server response : ${response.data}`);
      console.log(response);
      await getChats();

      // close add chat section
      setNewChatOpen(false);
  }

    return (
      <div className="conversation-list" >
        <Toolbar
          title="Messenger"
          leftItems={[
            <ToolbarButton key="cog" onClick={() => console.log("properties open")} icon="ion-ios-cog" />
          ]}
          rightItems={[
            <ToolbarButton key="add"  onClick={() => setNewChatOpen(true)} icon="ion-ios-add-circle-outline" />
          ]}
        />
          <AddChat disabled={!addChatOpened}  createChat={createChat}/>
        <ConversationSearch onclick={() => console.log("search")}  />
        {
          conversations.map(conversation =>
            <ConversationListItem
              key={conversation.name}
              data={conversation}
            />
          )
        }
      </div>
    );
}