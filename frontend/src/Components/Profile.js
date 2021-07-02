import React, {Component} from "react";
import logo from "../logo.svg";
import AuthService from "../services/AuthService";

class Profile extends Component {
    /*state = {
        records: []
    };

    async componentDidMount() {
        const response = await fetch('/auth/hello');
        const body = await response.json();
        this.setState({records: body});
    }*/
    render() {
        const currentUser = AuthService.getCurrentUser();
        return(
            <div className="container">
                <header className="jumbotron">
                    <h3>
                        <strong>{currentUser.username}</strong> Profile
                    </h3>
                </header>
                <p>
                    <strong>Token:</strong> {currentUser.token.substring(0, 20)} ...{" "}
                    {currentUser.token.substr(currentUser.token.length - 20)}
                </p>
                <p>
                    <strong>Id:</strong> {currentUser.id}
                </p>
                <strong>Authorities:</strong>
                <ul>
                    {currentUser.roles &&
                    currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
                </ul>
                <a href="/home">Go to home</a>
            </div>
        )
    }
}
export default Profile;