/*
Copyright 2022 Adobe. All rights reserved.
This file is licensed to you under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under
the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
OF ANY KIND, either express or implied. See the License for the specific language
governing permissions and limitations under the License.
*/
import React, { useEffect, useState } from "react";
import { AdaptiveForm } from "@aemforms/af-react-renderer";
import customMappings from './utils/mappings';
import ReactDOM from "react-dom";
import { Action, FormModel } from "@aemforms/af-core";
//@ts-ignore
import { Flex, Provider as Spectrum3Provider, View, defaultTheme } from '@adobe/react-spectrum'
import localFormJson from '../form-definations/form-model.json';
import { ToastContainer, ToastQueue } from "@react-spectrum/toast";

const getForm = async () => {
  if (process.env.USE_LOCAL_JSON == 'true') {
    return localFormJson;
  } else {
    let formAPI = process.env.FORM_API;
    // check for null or empty string
    if (!formAPI) {
      const SUFFIX = "jcr:content/guideContainer.model.json";
      const formPath = process.env.AEM_FORM_PATH
      formAPI = `${formPath}/${SUFFIX}`;
    }
    const resp = await fetch(formAPI);
    return (await resp.json());
  }
}

const Form = (props: any) => {
  const formStyle = {
    width: "50%"
  }
  const [form, setForm] = useState(undefined);

  const onSuccess = (action: any) => {
    let body = action.payload?.body;
    ToastQueue.positive(body.thankYouMessage.replace(/<(.|\n)*?>/g, ''));
    console.log("Thank you message", body.thankYouMessage.replace(/<(.|\n)*?>/g, ''));

  }

  const onError = (action: any) => {
    console.error("Error'ed when trying to send the form data to the server");
  }

  const onFailure = (action: any) => {
    console.error("Failed to send the form data to the server");
  }

  const onInitialize = (action: Action) => {
    console.log('Initializing Form');
  };

  const onFieldChanged = (action: Action) => {
    console.log("Field has changed")
  };

  useEffect(() => {
    getForm().then(json => {
      if ('afModelDefinition' in json) {
        setForm(json.afModelDefinition)
      } else {
        setForm(json)
      }
    });
  }, [setForm]);

  if (form) {
    const element = document.querySelector(".cmp-formcontainer__content")
    const retVal = (
      <Spectrum3Provider theme={defaultTheme} colorScheme="light">
        <Flex direction="column" width="100%">
          <View justifySelf="center" alignSelf="center">
            <AdaptiveForm style={formStyle} formJson={form} mappings={customMappings} onSubmitSuccess={onSuccess} onSubmitError={onError} onSubmitFailure={onFailure} onInitialize={onInitialize} onFieldChanged={onFieldChanged} />
          </View>
        </Flex>
        <ToastContainer />
      </Spectrum3Provider>
    )
    return ReactDOM.createPortal(retVal, element)
  }
  return null
}

export default Form