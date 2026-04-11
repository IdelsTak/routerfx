package com.github.idelstak.routerfx.proof;

public final class Main {

    private final ProofCli cli;

    Main(ProofCli cli) {
        this.cli = cli;
    }

    int run(String[] args) {
        return cli.run(args);
    }

    public static void main(String[] args) {
        var app = new Main(
          new ProofCli(
            new ConsolePasswordInput(System.in),
            new DefaultRouterApiFactory(),
            System.out,
            System.err
          )
        );
        var code = app.run(args);
        if (code != 0) {
            System.exit(code);
        }
    }
}
