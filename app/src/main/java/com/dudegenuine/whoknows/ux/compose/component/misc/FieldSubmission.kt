package com.dudegenuine.whoknows.ux.compose.component.misc

/**
 * Tue, 18 Jan 2022
 * WhoKnows by utifmd
 **/
/*
@Composable
fun FieldSubmission(
    modifier: Modifier = Modifier,
    fieldName: String,
    fieldValue: String,
    onFieldValueChange: (String) -> Unit,
    isLoading: Boolean = false,
    error: String,
    submitLabel: String,
    submitEnable: Boolean = false,
    onTailPressed: () -> Unit,
    onSubmitPressed: () -> Unit) {

    */
/*Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GeneralTopBar(
                title = fieldName,
                submitLabel = submitLabel,
                submitEnable = submitEnable*//*
*/
/*formState.isGetValid*//*
*/
/*,
                submitLoading = isLoading,
                onSubmitPressed = onSubmitPressed*//*
*/
/*viewModel::findRoom*//*
*/
/*
            )
        },
        content = { padding ->
            Column(
                modifier = modifier.padding(padding).fillMaxSize()) {

                GeneralTextField(
                    label = "Enter an invitation code",
                    value = fieldValue*//*
*/
/*formState.roomId*//*
*/
/*,
                    leads = Icons.Default.Security,
                    tails = if(fieldValue.isNotBlank()) Icons.Default.Close else null,
                    onTailPressed = onTailPressed*//*
*/
/*{ formState.onRoomIdChange("") }*//*
*/
/*,
                    onValueChange = onFieldValueChange*//*
*/
/*formState::onRoomIdChange*//*
*/
/*
                )

                if (error.isNotBlank()){

                    Spacer(modifier = modifier.height(12.dp))
                    ErrorScreen(message = error, isSnack = true, isDanger = false)
                }
            }
        }
    )*//*

}*/
