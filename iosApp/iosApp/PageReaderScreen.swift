//
//  PageReaderScreen.swift
//  iosApp
//
//  Created by Samuel Chiscari on 30/10/2026.
//

import SwiftUI

struct PageReaderScreen: View {
    @State private var showContent = false
    var body: some View {
        VStack {
            Button("Click me!") {}
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct PageReaderScreen_Previews: PreviewProvider {
    static var previews: some View {
        PageReaderScreen()
    }
}
