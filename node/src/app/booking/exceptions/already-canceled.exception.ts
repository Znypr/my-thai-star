import { BusinessLogicException } from '../../shared/exceptions/business-logic.exception';

export class AlreadyCancelledException extends BusinessLogicException {
  constructor() {
    super('Already cancelled');
  }
}
