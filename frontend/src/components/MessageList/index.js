import React, {useEffect, useState} from 'react';
import Compose from '../Compose';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import Message from '../Message';
import moment from 'moment';

import './MessageList.css';
import axios from "axios";

const MY_USER_ID = 'apple';

export default function MessageList(props) {
    const [chatId, setChatId] = useState(0);

    let messagesEnd;

    useEffect(() => {
        console.log("puzidex")
        renderMessages();
        scrollToBottom();
    },[props.messagesProp]);

    const scrollToBottom = () => {
        messagesEnd.scrollIntoView({ behavior: "smooth" });
    }

  const renderMessages = () => {
    let i = 0;
    let messageCount = props.messagesProp.length;
    let tempMessages = [];

    while (i < messageCount) {
      let previous = props.messagesProp[i - 1];
      let current = props.messagesProp[i];
      let next = props.messagesProp[i + 1];
      // let isMine = current.author === MY_USER_ID
        let isMine = props.messagesProp[i].author===props.username;
      let currentMoment = moment(current.timestamp);
      let prevBySameAuthor = false;
      let nextBySameAuthor = false;
      let startsSequence = true;
      let endsSequence = true;
      let showTimestamp = true;

      if (previous) {
        let previousMoment = moment(previous.timestamp);
        let previousDuration = moment.duration(currentMoment.diff(previousMoment));
        prevBySameAuthor = previous.author === current.author;
        
        if (prevBySameAuthor && previousDuration.as('hours') < 1) {
          startsSequence = false;
        }

        if (previousDuration.as('hours') < 1) {
          showTimestamp = false;
        }
      }

      if (next) {
        let nextMoment = moment(next.timestamp);
        let nextDuration = moment.duration(nextMoment.diff(currentMoment));
        nextBySameAuthor = next.author === current.author;

        if (nextBySameAuthor && nextDuration.as('hours') < 1) {
          endsSequence = false;
        }
      }

      tempMessages.push(
        <Message
          key={i}
          isMine={isMine}
          startsSequence={startsSequence}
          endsSequence={endsSequence}
          showTimestamp={showTimestamp}
          data={current}
        />
      );

      // Proceed to the next message.
      i += 1;
    }

    return tempMessages;
  }

    return(
      <div className="message-list">
        <Toolbar
          title="Conversation Title"
          rightItems={[
            <ToolbarButton key="info" onClick={() => console.log("checkInfo")} icon="ion-ios-information-circle-outline" />,
            <ToolbarButton key="video" onClick={() => console.log("checkInfo")} icon="ion-ios-videocam" />,
            <ToolbarButton key="phone" onClick={() => console.log("checkInfo")} icon="ion-ios-call" />
          ]}
        />

        <div className="message-list-container">
            {renderMessages()}
            <div style={{ float:"left", clear: "both" }}
                 ref={(el) => { messagesEnd = el; }}>
            </div>
        </div>


        <Compose sendMessage={props.sendMessage} rightItems={[
          <ToolbarButton key="photo" onClick={() => console.log("checkInfo")} icon="ion-ios-camera" />,
          <ToolbarButton key="image" onClick={() => console.log("checkInfo")} icon="ion-ios-image" />,
          <ToolbarButton key="audio" onClick={() => console.log("checkInfo")} icon="ion-ios-mic" />,
          <ToolbarButton key="money" onClick={() => console.log("checkInfo")} icon="ion-ios-card" />,
          <ToolbarButton key="games" onClick={() => console.log("checkInfo")} icon="ion-logo-game-client.controller-b" />,
          <ToolbarButton key="emoji" onClick={() => console.log("checkInfo")} icon="ion-ios-happy" />
        ]}/>
      </div>
    );
}