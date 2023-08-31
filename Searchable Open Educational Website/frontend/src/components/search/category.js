import React from "react";
import "./category.css"

export default function Category({ category, options, onChange, checkedStates }) {
    return (
        <div className="p-4">
            <div className="categoryName">{category}</div>
            <div className="options">
                {options.map((option, index) => (
                    <div>
                        <input checked={checkedStates && checkedStates.includes(option === "Formulario de Google" ? "Google Form" : option)}
                               onChange={e => onChange(option, e.currentTarget.checked)} className="input" key={index} type="checkbox"/>{option}
                    </div>
                ))}
            </div>
        </div>
    )
}
