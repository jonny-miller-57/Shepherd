import React, {Component} from 'react';

interface DropdownProps {
    elements: string[],
    type: string,
    key: string
    onSelect(selection: any): void,
}

/**
 * A dropdown list that allows the user to select a building on campus
 */
class Dropdown extends Component<DropdownProps> {

    render() {
        let key: number;
        this.props.type === "destination" ? key = 1 : key = 2;
        let option: JSX.Element[] = [];
        key === 1 ? option.push(<option value="destination" key={key.toString()}>destination</option>) : option.push(<option value="problem" key={key.toString()}>problem</option>)
        for (let index in this.props.elements) {
            let name = this.props.elements[index];
            key += 2;
            option.push(<option value={name} key={key.toString()}>{name}</option>);
        }

        return (
            <div>
                <select
                    name={this.props.type}
                    id={this.props.type}
                    onChange={(event) => this.props.onSelect(event.target.value)}>
                    {option}
                </select>
            </div>
        );
    }
}

export default Dropdown;