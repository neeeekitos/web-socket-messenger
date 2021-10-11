import React, {useState} from 'react';
import './Compose.css';

export default function Compose(props) {

    const [textMessage, setTextMessage] = useState("");

    const onFormSubmit = () => {
        console.log("send Message : " + textMessage)
        props.sendMessage(textMessage);
        setTextMessage("");
    }

    return (
      <div className="compose">

        <form onSubmit={onFormSubmit} action='#' className="compose-form">
            <input
                type="text"
                className="compose-input"
                placeholder="Type a message, @name"
                onChange={(e) => setTextMessage(e.target.value)}
                value={textMessage}
            />
        </form>
          <div className="compose-icons">
              {
                  props.rightItems
              }
          </div>
      </div>
    );
}