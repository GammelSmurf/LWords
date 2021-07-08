import React from "react";
import {Route, Switch, withRouter} from "react-router-dom";
import Profile from "./Components/Profile";
import Settings from "./Components/Settings";
import Login from "./Components/Login";
import Home from "./Components/Home";
import MyNavBar from "./Components/MyNavBar";

import "./css/bootstrap-journal.min.css";
import "./css/recordTable.css";
import "./css/myNavBar.css";
import "./css/App.css"
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
                        <Route path='/home/settings' exact={true} component={Settings}/>
                        <Route path='/auth/signin' exact={true} component={Login}/>
                    </Switch>
                </div>
            </div>
        );
})

export default App;
