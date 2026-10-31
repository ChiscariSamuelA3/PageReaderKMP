//
//  PageReaderViewModelWrapper.swift
//  iosApp
//
//  Created by Samuel Chiscari on 31/10/2026.
//

import Foundation
import PageReaderKit

@MainActor
final class PageReaderViewModelWrapper: ObservableObject {
    @Published private(set) var state: PageState

    private let owner = PageReaderViewModelOwner()

    init() {
        state = owner.currentState
        owner.observe { [weak self] newState in
            MainActor.assumeIsolated { self?.state = newState }
        }
    }

    deinit {
        owner.viewModelStore.clear()
    }

    func onUrlChange(_ url: String) { owner.viewModel.onUrlChange(url: url) }
    func onSelectorChange(_ selector: String) { owner.viewModel.onSelectorChange(selector: selector) }
    func onReadClick() { owner.viewModel.onReadClick() }
}
