import React, {useState, useEffect} from 'react';

import './ConversationList.css';
import styles from "./addchat.module.css";
import Toolbar from "../Toolbar";
import ToolbarButton from "../ToolbarButton";
import ConversationSearch from "../ConversationSearch";
import ConversationListItem from "../ConversationListItem";
import ToggleButton from "./ToggleButton";
import {Form, Dropdown, Button, InputGroup} from "react-bootstrap";


export default function AddChat(props) {

    const [isGroup, setIsGroup] = useState(true);
    const [name, setName] = useState("");


    if (props.disabled) return null
    else return (
        <div className="add-chat">
            <Form className={styles.addChatForm}>
                <Dropdown className={styles.dropdown}>
                    <Dropdown.Toggle className={styles.dropdownToggle}>
                        {(isGroup) ? "Group ▼" : "One2one ▼"}
                    </Dropdown.Toggle>

                    <Dropdown.Menu className={styles.dropdownMenu}>
                        <Dropdown.Item href="#/o2o" className={styles.dropdownOption} onClick={() => setIsGroup(false)} active>One2one</Dropdown.Item>
                        <Dropdown.Item href="#/group" className={styles.dropdownOption} onClick={() => setIsGroup(true)} active>Group</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
                <input
                    type="search"
                    className="conversation-search-input"
                    placeholder={isGroup ? "Your group name" : "Username"}
                    value={name}
                    onChange={e => setName(e.target.value)}
                />
                <Button className={styles.btn} onClick={() => props.createChat(isGroup, name)}>Create</Button>
            </Form>
        </div>
    );
}
