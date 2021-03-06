import React from "react";
import AuthService from "../services/AuthService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser,faUsers,faSignOutAlt, faGraduationCap } from '@fortawesome/free-solid-svg-icons'
import { Navbar, Nav, Container } from 'react-bootstrap';

const MyNavBar = () =>{
    function logOut() {
        AuthService.logout();
    }
    const currentUser = AuthService.getCurrentUser();

        return(
            <Navbar bg="light" variant="light" expand="sm" className="navHeader">
                <Container>
                    <Navbar.Brand href="/home">LWords</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="navbarItems">
                            {currentUser.roles.includes('ADMIN') &&
                            <Nav.Link href="/home/users"><FontAwesomeIcon icon={faUsers} /> Users</Nav.Link>}
                            <Nav.Link href="/home/learning"><FontAwesomeIcon icon={faGraduationCap} /> Practise</Nav.Link>
                            <Nav.Link href="/home/profile"><FontAwesomeIcon icon={faUser} /> {currentUser.username}</Nav.Link>
                            <Nav.Link href="/auth/signin" onClick={logOut}><FontAwesomeIcon icon={faSignOutAlt}/> Logout</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
    )
}
export default MyNavBar;