import React from "react";
import "./contactus.css"
import axios from 'axios';
import Button from '@mui/material/Button';

export default class ContactUs extends React.Component {
    state = {
        name: '',
        subject: '',
        email: '',
        message: ''
      }
    constructor(props) {
        super(props);
        this.onEmailChange= this.onEmailChange.bind(this);
        this.onMsgChange= this.onMsgChange.bind(this);
        this.onNameChange= this.onNameChange.bind(this);
        this.onSubjectChange= this.onSubjectChange.bind(this);
        this.submitEmail= this.submitEmail.bind(this);
    }


    render() {
        return (
            <div className="container contact-section">
                <div className="row">
                    <div className="col-md-12">
                        <div className="section-title">
                            <h2 className="title">Contacto</h2>
                            <form id="contact-form" onSubmit={this.submitEmail}
                                method="POST">
                            <div className="form-group">
                            <div className="row">
                            <div className="col-md-6">
                                <input placeholder = "Nombre"  id="name" type="text"
                                   className="form-control" required value={this.state.name}
                                   //onChange={this.onNameChange.bind(this)}/>
                                   onChange= {this.onNameChange}/>
                            </div>
                            <div className="col-md-6">
                                <input placeholder = "Email"  id="email" type="email"
                                  className="form-control" aria-describedby="emailHelp"
                                  required value={this.state.email}
                                  //onChange=
                                  //{this.onEmailChange.bind(this)}
                                  onChange= {this.onEmailChange}
                                  />
                            </div>
                            </div>
                            </div>
                            <div className="form-group">
                                <input placeholder = "Asunto"  id="subject" type="text"
                                  className="form-control" required value={this.state.subject}
                                  //onChange={this.onSubjectChange.bind(this)}
                                  onChange= {this.onSubjectChange}/>
                            </div>
                            <div className="form-group">
                                <textarea placeholder = "Mensaje"  id="message"
                                   className="form-control" rows="10"
                                   required value={this.state.message}
                                  //onChange= {this.onMsgChange.bind(this)}
                                  onChange={this.onMsgChange}/>
                            </div>
                            <div style={{textAlign: "center"}}>
                                <Button variant="outlined" type="submit" id="button">ENVIAR</Button>
                            </div>
                            {/* <button className= "button"  >ENVIAR</button> */}
                            </form>
                        </div>
                    </div>

                </div>

                </div>
        );
    }



    onNameChange(event) {
        this.setState({name: event.target.value})
    }

    onEmailChange(event) {
        this.setState({email: event.target.value})
    }

    onSubjectChange(event) {
        this.setState({subject: event.target.value})
    }

    onMsgChange(event) {
        this.setState({message: event.target.value})
    }

    submitEmail(e){
        console.log('submitting');
        e.preventDefault();
        axios({
            method: "POST",
            url:"https://75vsbghrpd.execute-api.us-west-2.amazonaws.com/Prod/contact_us",
            headers: {'Content-Type': 'text/plain'}, // not using json to avoid CORS preflight
            data:  this.state
        }).then((response)=>{
          if (response.data.status === 'success'){
              alert("Message Sent.");
              this.resetForm()
          }else if(response.data.status === 'fail'){
              alert("Message failed to send.");
          }
        })
    }

    resetForm(){
        this.setState({name: '', email: '',subject:'', message: ''})
    }

}
