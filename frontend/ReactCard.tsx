import '@vaadin/vaadin-lumo-styles/icons.js';
import { Card } from '@vaadin/react-components/Card.js';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
import React from "react";
import {Icon} from "@vaadin/react-components";

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
                    <Card theme="cover-media">
                        {value != null ? (
                            <img slot="media" width="200" src={value} alt=""/>
                        ) : (
                            <Icon slot="media" icon="vaadin:question" className="bg-primary-10 text-primary" />
                        )}
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
          height: 200px
        }
        // .card-grid:hover {
        //   opacity: 1.0;
        // }
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
        }
        pre {
          overflow:auto
        }
      `}</style>
                </>
            );
        }
        else{
            return null
        }

    }

}
customElements.define('react-card', ReactCard);



