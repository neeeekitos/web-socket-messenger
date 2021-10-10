import React, { useState } from 'react';
import PropTypes from 'prop-types';
import './ToggleButton.css';


const ToggleButton = () => {

    const [toggle, setToggle] = useState(false);

    const triggerToggle = () => {
        setToggle(!toggle);
    }





    return(
        <div onChange={triggerToggle} className="wrg-toggle">
            <div className="wrg-toggle-container">
                <div className="wrg-toggle-check">
                    <span>group</span>
                </div>
                <div className="wrg-toggle-uncheck">
                    <span>one2one</span>
                </div>
            </div>
            <div className="wrg-toggle-circle"></div>
            <input className="wrg-toggle-input" type="checkbox" aria-label="Toggle Button" />
        </div>);
}

ToggleButton.propTypes = {
    disabled: PropTypes.bool,
    defaultChecked: PropTypes.bool,
    className: PropTypes.string,
    onChange: PropTypes.func,
    icons: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.shape({
            checked: PropTypes.node,
            unchecked: PropTypes.node
        })
    ])
};

export default ToggleButton;