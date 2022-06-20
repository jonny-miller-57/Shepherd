import React, {Component} from 'react';

interface DropdownProps {
    buildings: {[shortName: string]: string},
    type: string,
    key: string
    onSelect(selection: string): void,
}

/**
 * A dropdown list that allows the user to select a building on campus
 */
class Dropdown extends Component<DropdownProps> {

    render() {
        let key: number;
        this.props.type === "start" ? key = 1 : key = 2;
        let buildingOption: JSX.Element[] = [];
        key === 1 ? buildingOption.push(<option value="start" key={key.toString()}>Start</option>) : buildingOption.push(<option value="end" key={key.toString()}>End</option>)
        for (let shortName in this.props.buildings) {
            let longName = this.props.buildings[shortName];
            key += 2;
            buildingOption.push(<option value={shortName} key={key.toString()}>{longName} : {shortName}</option>);
        }

        return (
            <div>
                <select
                    name={this.props.type}
                    id={this.props.type}
                    onChange={(event) => this.props.onSelect(event.target.value)}>
                    {buildingOption}
                </select>
            </div>
        );
    }
}

export default Dropdown;