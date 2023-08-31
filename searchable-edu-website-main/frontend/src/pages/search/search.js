import React from "react";
import "./search.css";
import Result from "../../components/result/result"
import Grid from "@mui/material/Grid";
import Pagination from "@mui/material/Pagination";
import Category from "../../components/search/category";
import qs from "qs";

export default class Home extends React.Component {
  constructor(props) {
    // dummy data
    super(props);

    this.state = {
      searchResults: [],
      results_per_page: 5,
      page: 0,
      lastQuery: '',
      q: '',
      technology_used: '',
      level: '',
      skills: '',
    }

    this.changePage = this.changePage.bind(this);

    console.log("init", this.state);
  }

  isObjEmpty = obj => Object.keys(obj).length === 0;

  queryData = async (query) => {
    const search_endpoint = "https://75vsbghrpd.execute-api.us-west-2.amazonaws.com/Prod/search";
    console.log("Searching with", query)
    await fetch(search_endpoint, {
      method: 'POST',
      headers: {'Content-Type': 'text/plain'}, // not using json to avoid CORS preflight
      body: JSON.stringify(query),
    }).then(response => response.json())
      .then(resp => {
        if (typeof resp.length === "undefined") throw EvalError("Invalid result from the server");
        console.log(`Got ${resp.length} results`);
        this.setState({searchResults: resp});
      })
      .catch(error => console.error(`Error searching with query ${query}`, error));
  }

  async componentDidMount() {
    console.log("Running")
    // await this.queryData({q: ""});
    this.setQueryAndSearch(this.props.location.search);

    this.unlisten = this.props.history.listen((location, action) => {
      if (!location) return;
      this.setQueryAndSearch(location.search);
      console.log("History change", action, location);
    });
  }

  setQueryAndSearch = searchStr => {
    const query = qs.parse(searchStr, {ignoreQueryPrefix: true});
    if (this.isObjEmpty(query)) query.q = '';
    this.queryData(query);
    for (const val of ['q', 'technology_used', 'level', 'skills']) {
      if (!(val in query)) query[val] = '';
    }
    query.lastQuery = searchStr;
    this.setState(query);
  };

  componentWillUnmount() {
    if (this.unlisten) this.unlisten();
  }

  setSearchQuery = (e) => {
    this.setState({q: e.target.value});
  }

  setMultiFieldState = state => {
    const newState = {};
    const checked = state.checked;
    delete state.checked;
    const [field, val] = Object.entries(state)[0];
    let oldVal = this.state[field];
    if (checked) { // add
      if (oldVal) {
        if (typeof oldVal === 'string') oldVal = [oldVal];
        newState[field] = oldVal.concat(val);
      } else {
        newState[field] = val;
      }
    } else if (oldVal) { // remove
      if (!Array.isArray(oldVal)) newState[field] = ''; // single str, remove
      else {
        const idx = oldVal.indexOf(val);
        if (idx !== -1) oldVal.splice(idx, 1);
        if (oldVal.length === 1) oldVal = oldVal[0];
        newState[field] = oldVal;
      }
    } else { // shouldn't happen, but do it anyways :)
      newState[field] = '';
    }
    console.log("Set multi field state", newState, {...state, checked: checked});
    this.setState(newState);
  }

  setSearchTechnologyUsed = (e, checked) => {
    if (e === "Formulario de Google") {
      e = "Google Form"
    }
    this.setMultiFieldState({technology_used: e, checked: checked});
  }

  setSearchLevel = (e, checked) => {
    this.setMultiFieldState({level: e, checked: checked});
  }

  setSearchSkills = (e, checked) => {
    this.setMultiFieldState({skills: e, checked: checked});
  }

  changePage = (event, value) => {
    this.setState({page: value - 1});
  };

  startSearch = (firstTime = false) => {
    firstTime = firstTime === true; // check dedicated boolean
    let query = {
      q: this.state.q,
      technology_used: this.state.technology_used,
      level: this.state.level,
      skills: this.state.skills,
    }

    for (const [k, v] of Object.entries(query))
      if (!v || v.length === 0) delete query[k];

    let newPath;
    const queryStr = qs.stringify(query);
    if (!firstTime && queryStr === this.state.lastQuery) return; // skip if identical
    this.setState({lastQuery: queryStr});
    newPath = queryStr && queryStr.trim().length > 0 ? `/search?${queryStr}` : "/search";
    if(this.isObjEmpty(query)) query.q = '';
    if (firstTime) {
      console.log("First time searching", query);
      this.queryData(query);
    }
    else if (this.props.history.location.pathname + this.props.history.location.search !== newPath) {
      this.props.history.push(newPath);
      console.log("Navigating to path:", newPath, firstTime)
    }

    // should trigger history listener instead
    // this.queryData(query);
  }

  checkEnter = (e) => {
    if (e.keyCode === 13) {
      this.startSearch();
    }
  }

  render() {
    return (
      <div className="h-100">
        <Grid className="h-100" container spacing={1}>
          <Grid item xs={3} style={{minWidth: "300px"}}>
            <div className="p-2 h-100">
              <div className="searchBar">
                <input value={this.state.q} onChange={this.setSearchQuery} placeholder={"Search keywords..."}
                       onKeyDown={this.checkEnter}/>
                <button onClick={this.startSearch} className="btn btn-primary searchButton">Search</button>
              </div>
              <Category onChange={this.setSearchTechnologyUsed} category="Tecnología" checkedStates={this.state.technology_used}
                        options={["Video", "Peardeck", "Formulario de Google", "Quizlet"]} />
              <Category onChange={this.setSearchLevel} category="Nivel" checkedStates={this.state.level}
                        options={["BÁSICO", "INTERMEDIO", "AVANZADO"]}/>
              <Category onChange={this.setSearchSkills} category="Destrezas" checkedStates={this.state.skills}
                        options={["Comprensión auditiva", "Conversación", "Escritura", "Lectura"]}/>
            </div>
          </Grid>
          <Grid item xs={9}>
            {/* bg-light */}
            <div className="resultsTitle">
              {this.state.searchResults.length > 0 ? (this.state.searchResults.slice(this.state.results_per_page * this.state.page, this.state.results_per_page * this.state.page + this.state.results_per_page).map((data, index) =>
                  <Result key={index} data={data}/>)) : (<div>Lo sentimos. No se han encontrado resultados.</div>) // empty result
              }
              <Pagination count={parseInt(this.state.searchResults.length / this.state.results_per_page)}
                          color="standard" onChange={this.changePage}/>
            </div>
          </Grid>
        </Grid>
      </div>
    );
  }
}
