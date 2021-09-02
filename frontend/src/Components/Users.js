import React, {useEffect, useState} from 'react'
import BootstrapTable from "react-bootstrap-table-next";
import AuthService from "../services/AuthService";
import {Button, Form, Modal} from "react-bootstrap";


const Users = (props) => {
    const [content, setContent] = useState([]);
    const [currentUserId, setCurrentUserId] = useState(0);
    const [currentUserName, setCurrentUserName] = useState('');
    const [modalActive, setModalActive] = useState(false);
    const handleClose = () => setModalActive(false);
    const currentUser = AuthService.getCurrentUser();

    useEffect(() => {
        AuthService.getUsers().then(
            (response) => {
                let dataPrev = [];
                response.data.forEach(item => {
                    dataPrev.push({id:item.id, username: item.username, date: parseDate(item.date), roles: parseRoles(item.roles)})
                });
                setContent(dataPrev);
            },
            (error) => {
                const _content =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                setContent(_content);
            }
        );
    }, []);

    const columns = [{
        dataField: 'username',
        text: 'Username',
        style: { width: "50%" }
    }, {
        dataField: 'roles',
        text: 'Roles',
        style: { width: "50%" }
    }];

    const parseDate = (itemDateTime) => {
        const dateTime = new Date(itemDateTime);
        const options = { year: '2-digit', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit', second: '2-digit'};
        const dateTimeFormat = new Intl.DateTimeFormat('ru-RU', options).format;
        return dateTimeFormat(dateTime);
    }

    const parseRoles = (roles) => {
        let strRoles = '';
        roles.forEach(role=> {
            strRoles += role.name + ', ';
        });
        return strRoles.slice(0,-2);
    }

    const expandRow = {
        onlyOneExpanding: true,
        renderer: row => (
            <div>
                <div className="expandRowUnderLine">
                    <div className="expandRowDate">
                        <p><b>Registered:</b> {row.date}</p>
                    </div>
                    <div className="expandRowDeleteButton">
                        {currentUser.id !== row.id ?
                            <button className="btn btn-danger" onClick={() => handleClickDeleteUser(row.id, row.username)}>Delete</button>
                        : null}

                    </div>
                </div>

            </div>
        )
    };

    const rowStyle = (row, rowIndex) => {
        const style = {};
        if (row.id === currentUser.id) {
            style.backgroundColor = 'rgba(235, 104, 100, 0.3)';
        }
        return style;
    };

    const handleNewUser = () => {
        props.history.push('/auth/signup');
    }

    const handleClickDeleteUser = (id,username) => {
        setCurrentUserId(id)
        setCurrentUserName(username)
        setModalActive(true);
    }

    const handleRemoveUser = () => {
        AuthService.removeUser(currentUserId);
    }

    return(
        <div className="container">
            <div style={{marginTop: '40px'}}>
                <div className="standardPageHeader">
                    <h3>Users</h3>
                    <hr />
                </div>
                <BootstrapTable keyField='id' data={ content } columns={ columns } expandRow={expandRow} rowStyle={rowStyle}/>
                <button className="btn btn-primary addUser" onClick={handleNewUser}>Add new user</button>
            </div>

            <Modal show={modalActive} onHide={handleClose}
                   animation={true}>
                <Modal.Header>
                    <Modal.Title>Confirmation</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Text>
                        Do you really want to delete user {currentUserName}?
                    </Form.Text>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        No
                    </Button>
                    <Button variant="primary" onClick={handleRemoveUser}>
                        Yes
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>


    )
}

export default Users