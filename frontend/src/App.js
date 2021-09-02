import React from "react";
import {Route, Switch, withRouter} from "react-router-dom";
import Profile from "./Components/Profile";
import Login from "./Components/Login";
import Home from "./Components/Home";
import MyNavBar from "./Components/MyNavBar";
import Register from "./Components/Register";
import Users from "./Components/Users";
import Learning from "./Components/Learning";

import "./css/bootstrap-journal.min.css";
import "./css/home.css";
import "./css/myNavBar.css";
import "./css/App.css"
import "./css/users.css"
import "./css/learning.css"
import "./css/profile.css"
import 'react-bootstrap-table2-toolkit/dist/react-bootstrap-table2-toolkit.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';


const App = withRouter(({location})=> {
        return (
            <div>
                {location.pathname !== '/auth/signin' && <MyNavBar />}
                <div>
                    <Switch>
                        <Route path='/home' exact={true} component={Home}/>
                        <Route path='/home/profile' exact={true} component={Profile}/>
                        <Route path='/home/users' exact={true} component={Users}/>
                        <Route path='/home/learning' exact={true} component={Learning}/>
                        <Route path='/auth/signin' exact={true} component={Login}/>
                        <Route path='/auth/signup' exact={true} component={Register}/>
                    </Switch>
                </div>
            </div>
        );
})

export default App;
