import { withRuleEngine } from '../../utils/RuleEngineHook';
import React from 'react';

const ImageComponent = (props: any) => {
    const { value } = props;
    return (<img src={value}> </img>)
}

// wrapper component to wrap adaptive form capabilities
const AdaptiveImageComponent = (props: any) => {
    const { handlers, ...state } = props
    return <ImageComponent {...state} />;
}

export default withRuleEngine(AdaptiveImageComponent);