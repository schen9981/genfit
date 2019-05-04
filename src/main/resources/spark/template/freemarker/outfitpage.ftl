<#assign content>
    <h1>Outfits in <span class="name"></span>'s Closet</h1>

    <div id="outfits">
    </div>

    <div id="add">
        <div>
            <button id="suggestOutfit">Build your outfit!</button>
        </div>
        <div id="suggestOutfitModal" class="modal">
            <div class="modal-content">
                <div id="suggestSpan" class="close">&times;</div>
                <div id="suggestDiv">
                    <form id="suggestOutfitForm">
                        <div class="tab-suggest" id="outfit-info">
                            <div class="add-suggest" id="outer-item">
                                <button id="suggest-outer-item"
                                        onclick="navigateToSuggestTab(event, 1)">
                                    Add
                                    Outer
                                </button>
                            </div>

                            <div class="add-suggest" id="top-item">
                                <button id="suggest-top-item"
                                        onclick="navigateToSuggestTab(event, 2)">
                                    Add
                                    Top
                                </button>
                            </div>
                            <div class="add-suggest" id="bottom-item">
                                <button id="suggest-bottom-item"
                                        onclick="navigateToSuggestTab(event, 3)">
                                    Add
                                    Bottom
                                </button>
                            </div>
                            <div class="add-suggest" id="shoes-item">
                                <button id="suggest-shoes-item"
                                        onclick="navigateToSuggestTab(event, 4)">
                                    Add
                                    Shoes
                                </button>
                            </div>
                        </div>

                        <div class="tab-suggest" id="outer-select">
                            <div class="description">
                                <p>Select an outer:</p><br>
                            </div>
                            <div class="item-display">
                            </div>
                        </div>

                        <div class="tab-suggest" id="top-select">
                            <div class="description">
                                <p>Select a top:</p><br>
                            </div>
                            <div class="item-display">
                            </div>
                        </div>

                        <div class="tab-suggest" id="bottom-select">
                            <div class="description">
                                <p>Select a bottom:</p><br>
                            </div>
                            <div class="item-display">
                            </div>
                        </div>

                        <div class="tab-suggest" id="shoes-select">
                            <div class="description">
                                <p>Select a pair of shoes:</p><br>
                            </div>
                            <div class="item-display">
                            </div>
                        </div>

                        <div class="tab-suggest" id="display-suggestions">
                            <div id="display-outer-suggestions">
                                <h3>Outer Suggestions:</h3>
                            </div>
                            <div id="display-top-suggestions">
                                <h3>Top Suggestions:</h3>
                            </div>
                            <div id="display-bottom-suggestions">
                                <h3>Bottom Suggestions:</h3>
                            </div>
                            <div id="display-shoes-suggestions">
                                <h3>Shoes Suggestions:</h3>
                            </div>
                        </div>

                        <div style="overflow:auto;">
                            <button id="suggest">Generate Suggestions</button>
                            <div style="float:right;">
                                <button type="button" id="addFromSuggest"
                                        onclick="addItemFromSuggestions(event)">
                                    Add
                                    Suggestions to Outfit
                                </button>
                                <button type="button" id="addItemSuggest"
                                        onclick="addItemToOutfitSuggest(event)">
                                    Add
                                    to Outfit
                                </button>
                                <button type="button" id="backSuggest"
                                        onclick="navigateToSuggestTab(event, 0)">
                                    Back
                                </button>
                                <button type="button" id="addOutfitSuggest">Add
                                    Outfit
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</#assign>
<#include "main.ftl">
