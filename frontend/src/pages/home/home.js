import React from "react";
import "./home.css";

export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            searchQuery: ''
        };
    }

    startSearch = () => {
        const searchQuery = this.state.searchQuery;
        console.log("search clicked", searchQuery);
        if(searchQuery && searchQuery.trim().length > 0) this.props.history.push(`/search?q=${searchQuery}`);
        else this.props.history.push(`/search`);
    }

    updateSearchQuery = e => {
        this.setState({
            searchQuery: e.target.value
        });
    }

    checkEnter = e => {
        if (e.keyCode === 13){
            this.startSearch();
        }
    }

    render() {
        return (
            <div className="container-fluid">
                <div className = "row body">
                    <div className="bg-image">
                        <div className="bg-bg">
                            <div className="bg-text">
                                <div className="title-home">
                                    Oerinspanish <br/>
                                </div>
                                Recursos educativos de acceso libre <br/>
                                para la enseñanza del español <br/> <br/> <br/>
                                <div className="search">
                                    <input className="input" placeholder= "búsqueda" onChange={this.updateSearchQuery} onKeyDown={this.checkEnter}/>
                                    <button className="button" onClick={this.startSearch}/>
                                    {/*<Link style={{color: 'inherit'}}*/}
                                    {/*      to="/search">búsqueda</Link><br/>*/}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
