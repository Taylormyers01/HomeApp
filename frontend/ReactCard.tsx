import '@vaadin/vaadin-lumo-styles/icons.js';
import { Card } from '@vaadin/react-components/Card.js';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import React from "react";

class ReactCard extends ReactAdapterElement {

    protected render(hooks: RenderHooks): React.ReactElement | null {
        const [title, setTitle] = hooks.useState<string>('title');
        const [value, setValue] = hooks.useState<string>('value');
        const [subtitle, setSubtitle] = hooks.useState<string>('subtitle');
        const[type] = hooks.useState<string>('type')


        if (type == 'ICON'){
            return (
                <div className="card-grid">
                    {/* tag::snippet[] */}
                    <Card theme="cover-media outlined">
                        <img slot="media" width="200" src={value} alt=""/>
                        {/*style={{ textAlign: "center" }}*/}
                        <div slot="title">{title}</div>
                        {subtitle != null ? (
                            <div slot="subtitle">{subtitle}</div>
                        ) : null}
                    </Card>

                    <style>{`
        .card-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
          gap: 1em;
          width: 160px
          max-height: 300px
          max-width: 300px;
        }
        .card-grid:hover {
          opacity: .8;
        }
        img {
            height: 200px;
        }
      `}</style>
                </div>
            );
        }
        if(type == 'CONTENT') {
            return (
                <>
                    <Card>
                        <div slot="header" className="leading-xs">
                            <h2>{value}</h2>
                            <div className="uppercase text-xs text-secondary">{title}</div>
                        </div>
                        <pre>{subtitle}</pre>
                    </Card>
                    <style>{`
        vaadin-card {
          max-width: 300px;
          // min-height: 300px;
          height: 300px;
        }
        pre {
          overflow:auto
        }
      `}</style>tgy
                </>
            );
        }
        else{
            return null
        }

    }

}
customElements.define('react-card', ReactCard);



