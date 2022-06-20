/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import Dropdown from "./Dropdown";
import Map from "./Map";
import type {Edge} from "./Edge";
import {CampusBuilding} from "./CampusBuilding";

interface AppState {
    edges: Edge[];
    buildings: CampusBuilding[];
    buildingNames: {[shortName: string]: string};
    start: string;
    end: string;
}

class App extends Component<{}, AppState> { // <- {} means no props
    constructor(props: any) {
        super(props);
        this.state = {
            edges: [],
            buildings: [],
            buildingNames: {},
            start: "start",
            end: "end",
        };
    }

    componentDidMount() {
        this.getBuildingNames();
        this.getBuildingPoints();
    }

    getBuildingNames = async () => {
        try {
            let response = await fetch("http://localhost:4567/buildingnames");
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }
            let names = await response.json();
            this.setState({buildingNames: names});
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    getBuildingPoints = async () => {
        try {
            let response = await fetch("http://localhost:4567/buildingpoints");
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }
            let points = await response.json();
            this.setState({buildings: points});
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    getStartAndEndPoints = async () => {
        try {
            let response = await fetch(`http://localhost:4567/buildingpoints-start-end?start=${this.state.start}&end=${this.state.end}`);
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }
            let points = await response.json();
            this.setState({buildings: points});
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    selectStart = (selection: string) => {
        this.setState({start: selection});
    }

    selectEnd = (selection: string) => {
        this.setState({end: selection});
    }

    setBuilding = (selection: string) => {
        if (this.state.start === "start") {
            this.selectStart(selection);
        } else {
            this.selectEnd(selection);
            this.findPath();
        }
    }

    clear = () => {
        this.setState({edges: [], buildingNames: {}, start: "start", end: "end"});
        this.getBuildingNames();
        this.getBuildingPoints();
    }

    findPath = async () => {
        try {
            if (this.state.start === "start" || this.state.end === "end") {
                alert("you must enter a valid start and end building");
                return;
            }
            let response = await fetch(`http://localhost:4567/findpath?start=${this.state.start}&end=${this.state.end}`);
            let edgeJSON = await response.json();
            this.setState({edges: edgeJSON});
            this.getStartAndEndPoints();
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    render() {

        return (
            <div>
                <h1 id="app-title">Find Shortest Path</h1>
                <div style={{float:"left"}}>
                    <div>
                        <h4 style={{marginBottom:10}}>Click two buildings</h4>
                        <input className="e-input" type="text" placeholder={this.state.start} disabled={true}/>
                        <label className="e-float-text">Start</label>
                    </div>
                    <div>
                        <input className="e-input" type="text" placeholder={this.state.end} disabled={true}/>
                        <label className="e-float-text">End</label>
                    </div>
                    <button onClick={this.clear}>Clear</button>
                    <h3>- Or -</h3>
                    <div>
                        <h4 style={{marginBottom:10}}>Select buildings from the dropdowns</h4>
                        <Dropdown
                            key={"1"}
                            buildings={this.state.buildingNames}
                            type="start"
                            onSelect={this.selectStart}
                        />
                        <Dropdown
                            key={"2"}
                            buildings={this.state.buildingNames}
                            type="end"
                            onSelect={this.selectEnd}
                        />
                        <button onClick={() => {this.findPath()}}>Go</button>
                        <button onClick={() => {this.clear()}}>Clear</button>
                    </div>
                </div>
                <div>
                    <Map
                        edges={this.state.edges}
                        buildings={this.state.buildings}
                        onClick={this.setBuilding}
                    />
                </div>
            </div>
        );
    }
}

export default App;
