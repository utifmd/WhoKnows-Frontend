package com.dudegenuine.whoknows.ux.vm.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dudegenuine.model.Search
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IAppUseCaseModule.Companion.EMPTY_STRING
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.compose.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sun, 12 Jun 2022
 * WhoKnows by utifmd
 **/

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel
    @Inject constructor(
    override val caseRoom: IRoomUseCaseModule,
    override val caseUser: IUserUseCaseModule): ISearchViewModel() {
    private val TAG: String = javaClass.simpleName
    override val currentUserId: String = caseRoom.preferences.userId

    private val _searchField = mutableStateOf(TextFieldValue(""))
    val field get() = _searchField.value
    private val _searchType = mutableStateOf<SearchState>(SearchState.User)
    val type get() = _searchType.value

    private val _searchTextFlow = MutableStateFlow<SearchState?>(null)
    private val searchTextFlow get() = _searchTextFlow

    override val pagingFlow: Flow<PagingData<Search<*>>> = searchTextFlow
        .flatMapLatest{ param -> when(param) {
            is SearchState.User -> caseUser.searchUser(field.text)
            is SearchState.Room -> caseRoom.searchRooms(field.text)
            else -> emptyFlow() }}
        .cachedIn(viewModelScope)

    fun onSearchTextChange(text: String){
        onSearchTextFlowChange(null)

        _searchField.value = TextFieldValue(text)
    }
    private fun onSearchTypeChange(type: SearchState){
        _searchType.value = type
    }
    private fun onSearchTextFlowChange(state: SearchState?){
        viewModelScope.launch { _searchTextFlow.emit(state) }
    }
    fun onChipUserPressed() = with(SearchState.User){
        onSearchTypeChange(this)
        onSearchTextFlowChange(this)
    }
    fun onChipRoomPressed() = with(SearchState.Room){
        onSearchTypeChange(this)
        onSearchTextFlowChange(this)
    }
    fun onClearFieldPressed() {
        onSearchTextChange(EMPTY_STRING)
        onSearchTextFlowChange(null)
    }
    override fun onBackPressed() = onNavigateBack()
    override fun onSearchButtonPressed() = with(type){
        onSearchTypeChange(this)
        onSearchTextFlowChange(this)
    }
}