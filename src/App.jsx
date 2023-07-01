import { useState } from 'react'
import './App.css'

function App() {
  const[field1,setField1] = useState("")

  const handleInput1Change = value => {setField1(value)}
  const handle_button1_click = (e) => {console.log('=== Clicked button_1: '+field1)}

  return (
    <div className='root'>
      <div className='frame'>
        <input type='text' className='input1_style' value={field1} onChange={e => handleInput1Change(e.target.value)}/>
        <br/>
        <br/>
        <button className='button1_style' onClick={(e) => handle_button1_click(e)}>ENVIAR</button>
      </div>
    </div>
  )
}

export default App
