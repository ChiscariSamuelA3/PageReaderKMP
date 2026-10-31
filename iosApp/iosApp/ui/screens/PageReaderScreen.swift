//
//  PageReaderScreen.swift
//  iosApp
//
//  Created by Samuel Chiscari on 30/10/2026.
//

import SwiftUI
import PageReaderKit

struct PageReaderScreen: View {
    @StateObject private var wrapper = PageReaderViewModelWrapper()

    var body: some View {
        PageReaderContent(
            state: wrapper.state,
            onUrlChange: wrapper.onUrlChange,
            onSelectorChange: wrapper.onSelectorChange,
            onReadClick: wrapper.onReadClick
        )
    }
}

private struct PageReaderContent: View {
    let state: PageState
    let onUrlChange: (String) -> Void
    let onSelectorChange: (String) -> Void
    let onReadClick: () -> Void

    private var isLoading: Bool { state.status is ReadStatusLoading }
    private var isError: Bool { state.status is ReadStatusFailure }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("PageReader")
                        .font(.largeTitle.bold())
                    Text("Extract text from any web page using a CSS selector.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }

                SectionCard {
                    Text("URL")
                        .font(.subheadline.weight(.medium))
                        .foregroundStyle(.secondary)
                    
                    InputField(icon: "link", text: Binding(get: { state.url }, set: onUrlChange), keyboard: .URL)
                    
                    Text("CSS selector")
                        .font(.subheadline.weight(.medium))
                        .foregroundStyle(.secondary)
                    
                    InputField(
                        icon: "chevron.left.forwardslash.chevron.right",
                        text: Binding(get: { state.selector }, set: onSelectorChange)
                    )
                    
                    Button(action: onReadClick) {
                        HStack(spacing: 8) {
                            if isLoading {
                                ProgressView().tint(.white)
                            }
                            Text(isLoading ? "Reading…" : "Read")
                                .font(.headline)
                        }
                        .frame(maxWidth: .infinity, minHeight: 36)
                    }
                    .buttonStyle(.borderedProminent)
                    .buttonBorderShape(.roundedRectangle(radius: 10))
                    .disabled(isLoading)
                    .padding(.top, 4)
                }

                SectionCard {
                    Text("Result")
                        .font(.subheadline.weight(.medium))
                        .foregroundStyle(.secondary)
                    
                    TextField("The extracted text will appear here.", text: .constant(resultText), axis: .vertical)
                        .lineLimit(4...)
                        .foregroundStyle(isError ? Color.red : Color.primary)
                        .allowsHitTesting(false)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 16)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(isError ? Color.red : Color.gray, lineWidth: 1)
                        )
                }
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 24)
        }
        .scrollDismissesKeyboard(.interactively)
        .background(Color(.systemGroupedBackground))
    }

    private var resultText: String {
        switch state.status {
        case let success as ReadStatusSuccess: success.text
        case let failure as ReadStatusFailure: errorMessage(failure.error)
        default: ""
        }
    }
}

private func errorMessage(_ error: PageReaderError) -> String {
    switch error {
    case is PageReaderErrorInvalidUrl: "Please enter a valid URL."
    case is PageReaderErrorInsecureUrl: "Only HTTPS URLs are allowed."
    case is PageReaderErrorInvalidSelector: "The CSS selector is not valid."
    case let error as PageReaderErrorNetwork: "Network error: \(error.reason)"
    case let error as PageReaderErrorHttp: "The server responded with HTTP \(error.code)."
    case is PageReaderErrorResponseTooLarge: "The page is too large (limit is 2 MB)."
    case let error as PageReaderErrorNothingFound: "No text found for \"\(error.selector)\"."
    default: "Something went wrong."
    }
}

#Preview {
    PageReaderContent(
        state: PageState(
            url: "https://example.com",
            selector: "title",
            status: ReadStatusSuccess(text: "Example Domain")
        ),
        onUrlChange: { _ in },
        onSelectorChange: { _ in },
        onReadClick: {}
    )
}
