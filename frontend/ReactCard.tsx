import '@vaadin/vaadin-lumo-styles/icons.js';
import { Card } from '@vaadin/react-components/Card.js';
import {ReactAdapterElement, RenderHooks} from "Frontend/generated/flow/ReactAdapter";
// import img from '../src/main/resources/images/question_mark.jpg';
import React from "react";
import {Icon} from "@vaadin/react-components";

class ReactCard extends ReactAdapterElement {
    // '../src/main/resources/images/question_mark.jpg'

    protected render(hooks: RenderHooks): React.ReactElement | null {
        const [title, setTitle] = hooks.useState<string>('title');
        const [value, setValue] = hooks.useState<string>('value');
        const [subtitle, setSubtitle] = hooks.useState<string>('subtitle');

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

}
customElements.define('react-card', ReactCard);



