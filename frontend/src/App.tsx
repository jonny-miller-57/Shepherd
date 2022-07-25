import React, {Component} from 'react';

import "./App.css";
import {BoulderProblem} from "./BoulderProblem";
import Dropdown from "./Dropdown";

interface AppState {
    destinations: string[];
    destination: string;
    problems: string[];
}

class App extends Component<{}, AppState> { // <- {} means no props
    constructor(props: any) {
        super(props);
        this.state = {
            destinations: [],
            destination: "select",
            problems: [],
        };
    }

    componentDidMount() {
        this.getDestinations();
    }

    getDestinations = async () => {
        try {
            let response = await fetch("http://localhost:4567/destinations");
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }
            let destinations = await response.json();
            this.setState({destinations: destinations});
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    getProblems = async () => {
        try{
            let response = await fetch(`http://localhost:4567/problems?dest=${this.state.destination}`);
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }
            let problems = await response.json();
            this.setState({problems: problems});
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    selectDest = (selection: string) => {
        this.setState({
            destination: selection
        }, () => {
            this.getProblems();
        });

    }

    selectProblem = (selection: BoulderProblem) => {

    }

    clear = () => {
        this.setState({
            destination: "select",
            destinations: [],
            problems: [],
        }, () => {
            this.getDestinations();
        });
    }

    render() {
        return (
            <div>
                <h1 id="app-title">Shepherd</h1>
                    <h4 style={{marginBottom:10}}>Select Destination</h4>
                    <Dropdown
                        elements={this.state.destinations}
                        type="destination"
                        key={"1"}
                        onSelect={this.selectDest}
                    />
                    <Dropdown
                        elements={this.state.problems}
                        type={"problem"}
                        key={"2"}
                        onSelect={this.selectProblem}
                    />
                    <button onClick={() => {this.clear()}}>Clear</button>
            </div>
        );
    }
}

export default App;
