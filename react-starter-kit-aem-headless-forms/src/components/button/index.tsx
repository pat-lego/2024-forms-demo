import Button from '@material-ui/core/Button';
import React from 'react';
import { withRuleEngine } from '../../utils/RuleEngineHook';
import { Flex, View } from '@adobe/react-spectrum';

const ButtonComponent = (props: any) => {
    const { label, enabled, visible, onClick } = props;
    const isVisible = typeof visible === 'undefined' || visible;
    const isEnabled = enabled === false ? false : true;
    return isVisible ? (
        <Flex direction="column" width="100%">
            <View justifySelf="center" alignSelf="center">
                <Button
                    variant="contained"
                    color="primary"
                    size="medium"
                    onClick={onClick}
                    disabled={!isEnabled}
                >
                    {label?.value}
                </Button>
            </View>
        </Flex>
    ) : null;

}

// wrapper component to wrap adaptive form capabilities
const AdaptiveFormButton = (props: any) => {
    const { handlers, ...state } = props
    return <ButtonComponent {...state} onClick={handlers?.dispatchClick} />;
}


export default withRuleEngine(AdaptiveFormButton);
