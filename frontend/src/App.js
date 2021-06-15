import logo from './logo.svg';
import './App.css';
import Home from './Components/Home';
import React, {Component} from "react";
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                </Switch>
            </Router>
        );
    }
}

export default App;
