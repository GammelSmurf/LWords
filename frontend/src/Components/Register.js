import {Button, Form} from "react-bootstrap";
import React, {useState} from "react";
import AuthService from "../services/AuthService";

const Register = (props) => {
    const [validated, setValidated] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [responseTextVisibility, setResponseTextVisibility] = useState(false);

    const handleSubmit = (event) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            setValidated(true);
        }
        else {
            AuthService.register(username, password)
                .then(
                    () => {
                        props.history.push('/home/users')
                    },
                    () => {
                        setResponseTextVisibility(false);
                        setMessage('That username is already in use. Please choose another one...');
                        setResponseTextVisibility(true);
                    }
                );
        }
        event.preventDefault();
        event.stopPropagation();
    }

    const onChangeUsername = (e) => {
        setUsername(e.target.value);
    };

    const onChangePassword = (e) => {
        setPassword(e.target.value);
    };

    return (
        <div className="container">
            <div className="wrapper">
                <div className="standardPageHeader">
                    <h2>New user</h2>
                    <hr />
                </div>
                <Form noValidate validated={validated} onSubmit={handleSubmit} className="col-sm-4">
                    <Form.Group controlId="username">
                        <Form.Label><b>Username</b></Form.Label>
                        <Form.Control
                            required
                            type="text"
                            onChange={onChangeUsername}
                        />
                        <Form.Control.Feedback type="invalid">
                                The username cannot be empty!
                            </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group controlId="password">
                        <Form.Label><b>Password</b></Form.Label>
                        <Form.Control
                            required
                            type="password"
                            onChange={onChangePassword}
                        />
                        <Form.Control.Feedback type="invalid">
                                The password cannot be empty!
                            </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        {responseTextVisibility &&
                            <Form.Text className="text-danger responseText">
                                <b>{message}</b>
                            </Form.Text>
                        }
                    </Form.Group>
                    <Button type="submit" className="formSubmitButton">Submit</Button>
                </Form>
            </div>
        </div>
    )
}

export default Register;