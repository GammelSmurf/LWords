import './App.css';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Profile from "./Components/Profile";
import Login from "./Components/Login";
import Home from "./Components/Home";
import "bootstrap/dist/css/bootstrap.min.css";
import React from "react";

function App() {
    return (
      <BrowserRouter>
        <Switch>
            <Route path='/home' exact={true} component={Home}/>
          <Route path='/home/profile' exact={true} component={Profile}/>
          <Route path='/auth/signin' exact={true} component={Login}/>
        </Switch>
      </BrowserRouter>
  );
}

export default App;
